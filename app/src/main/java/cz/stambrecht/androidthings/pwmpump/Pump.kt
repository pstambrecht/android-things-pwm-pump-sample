/*
 *  Copyright (C) 2018 Bc. Pavel Stambrecht - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Pavel Stambrecht
 * /
 */

package cz.stambrecht.androidthings.pwmpump

/**
 * [Pump] extends [Peripheral] to define abstract pump
 *
 * @author Pavel Stambrecht
 * @date 04/02/2018
 */
abstract class Pump : Peripheral() {

    /**
     * Abstract pump method
     * @param timeMs pump time in ms
     */
    abstract fun pump(timeMs: Long)
}