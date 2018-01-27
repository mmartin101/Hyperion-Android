package com.willowtreeapps.hyperion.disk;

import android.support.annotation.NonNull;

import com.google.auto.service.AutoService;
import com.willowtreeapps.hyperion.plugin.v1.Plugin;
import com.willowtreeapps.hyperion.plugin.v1.PluginModule;

@AutoService(Plugin.class)
public class DiskPlugin implements Plugin {

    @NonNull
    @Override
    public PluginModule createPluginModule() {
        return new DiskModule();
    }

}