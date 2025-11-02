# Crypto SDK & KMP

The SDK is build for educational purpose and is NOT KMP ready now just to make it simple.
Frameworks used to build this SDK are KMP compatible and there should not be massive framework rebuild to make SDK KMP ready.


## Current platform locks
Here is the list of known Android platform locks in this SDK

### import android.icu.math.BigDecimal
Using BigDecimal for financial operations should be replaced by expect/actual or some kmp library like com.ionspin.kotlin.bignum

### import android.os.Parcelable
TBD.

