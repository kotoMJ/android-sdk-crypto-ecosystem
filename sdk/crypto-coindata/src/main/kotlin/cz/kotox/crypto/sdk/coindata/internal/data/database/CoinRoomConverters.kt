package cz.kotox.crypto.sdk.coindata.internal.data.database

import cz.kotox.crypto.sdk.coindata.internal.data.database.converters.IcuBigDecimalConverter
import cz.kotox.crypto.sdk.coindata.internal.data.database.converters.InstantConverter

internal class CoinRoomConverters :
    IcuBigDecimalConverter,
    InstantConverter
