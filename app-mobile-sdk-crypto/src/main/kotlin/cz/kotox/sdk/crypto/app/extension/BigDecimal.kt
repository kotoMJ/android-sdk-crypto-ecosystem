package cz.kotox.sdk.crypto.app.extension

import java.math.BigDecimal

// Enables the "-" operator
operator fun BigDecimal.minus(other: BigDecimal): BigDecimal {
    return this.subtract(other)
}

// You might want these too for future calculations
operator fun BigDecimal.plus(other: BigDecimal): BigDecimal = this.add(other)
operator fun BigDecimal.div(other: BigDecimal): BigDecimal = this.divide(other)
operator fun BigDecimal.times(other: BigDecimal): BigDecimal = this.multiply(other)
operator fun BigDecimal.compareTo(other: BigDecimal): Int = this.compareTo(other)
