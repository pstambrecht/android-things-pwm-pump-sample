/*
 *  Copyright (C) 2018 Bc. Pavel Stambrecht - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Pavel Stambrecht
 * /
 */

package cz.stambrecht.androidthings.pwmpump

/**
 * [Peripheral] represents abstract peripheral.
 *
 * @author Pavel Stambrecht
 * @date 04/02/2018
 */
abstract class Peripheral {
    /**
     * Indicates if peripheral is opened
     * @value true if opened
     */
    var isOpened: Boolean = false

    /**
     * Opens peripheral to be used
     */
    fun open(): Boolean {
        if (!isOpened) {
            isOpened = onOpen()
        }
        return isOpened
    }

    /**
     * Method containing logic to open peripheral
     * @return true if opened
     */
    protected open fun onOpen(): Boolean = true

    /**
     * Closes peripheral device if was opened
     */
    fun close() {
        if (isOpened) {
            onClose()
            isOpened = false
        }
    }

    /**
     * Method containing logic to close peripheral
     */
    protected open fun onClose() {
        //empty
    }
}