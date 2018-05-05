/*
 *  Copyright (C) 2018 Bc. Pavel Stambrecht - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Pavel Stambrecht
 * /
 */

package cz.stambrecht.androidthings.pwmpump

import android.os.Handler
import android.os.Looper
import com.google.android.things.pio.PeripheralManager
import com.google.android.things.pio.Pwm
import java.io.IOException

/**
 * [PwmPump] represents pump which is controlled by PWM signal
 *
 * @author Pavel Stambrecht
 * @date 04/02/2018
 */
class PwmPump(private val mConfig: Config) : Pump() {

    companion object {
        private const val FREQUENCY = 50.0
        private const val DUTY_FREQUENCY_START = 100.0
        private const val DUTY_FREQUENCY_RUN = 80.0
        private const val SWITCH_DUTY_FREQUENCY_DELAY_MS = 2500L
    }

    private val mMainHandler = Handler(Looper.getMainLooper())
    private val mPumpRunnable = Runnable {
        if (mIsRunning) {
            mPwm?.setPwmDutyCycle(DUTY_FREQUENCY_RUN)
        }
    }

    private var mPwm: Pwm? = null
    private var mIsRunning = false

    /**
     * Opens ans setup PWM connection
     * @return true if opened
     */
    override fun onOpen(): Boolean {
        val peripheralManager = PeripheralManager.getInstance()
        return try {
            mPwm = peripheralManager.openPwm(mConfig.pwmName)
            mPwm?.setPwmFrequencyHz(FREQUENCY)
            mPwm?.setPwmDutyCycle(DUTY_FREQUENCY_START)

            true
        } catch (e: IOException) {
            mPwm?.close()
            mPwm = null
            e.printStackTrace()
            false
        }
    }

    /**
     * Closes device
     */
    override fun onClose() {
        mMainHandler.post({
            mIsRunning = false
            mPwm?.close()
        })
    }

    /**
     * Starts pump request
     * @param timeMs pumping time in ms
     */
    override fun pump(timeMs: Long) {
        mMainHandler.post({
            mPwm?.let {
                it.setEnabled(true)
                mIsRunning = true
                if (timeMs > SWITCH_DUTY_FREQUENCY_DELAY_MS) {
                    mMainHandler.postDelayed(mPumpRunnable, SWITCH_DUTY_FREQUENCY_DELAY_MS)
                }

                mMainHandler.postDelayed({
                    mIsRunning = false
                    it.setEnabled(false)
                }, timeMs)
            }
        })

    }

    /**
     * [Config] class contains configuration for pump
     * @property pwmName name
     */
    class Config(val pwmName: String)
}