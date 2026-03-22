package cz.kotox.crypto.sdk.monitoring

import android.content.Context

public interface Monitoring {

    public class Builder(context: Context) : MonitoringBuilder(
        context = context,
    )

    public fun initMonitoring()

    public fun shutdown()
}
