package com.willowtreeapps.hyperion.core.internal;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.core.AttributeTranslator;
import com.willowtreeapps.hyperion.core.MeasurementHelper;
import com.willowtreeapps.hyperion.core.ViewTarget;
import com.willowtreeapps.hyperion.core.plugins.DrawerContainer;
import com.willowtreeapps.hyperion.core.plugins.OverlayContainer;
import com.willowtreeapps.hyperion.core.plugins.PluginExtension;

class PluginExtensionImpl implements PluginExtension {

    private final CoreComponent component;
    private final HyperionPluginView pluginView;

    PluginExtensionImpl(CoreComponent component, HyperionPluginView pluginView) {
        this.component = component;
        this.pluginView = pluginView;
    }

    @NonNull
    @Override
    public ViewGroup getContentRoot() {
        return (ViewGroup) component.getActivity().findViewById(android.R.id.content);
    }

    @NonNull
    @Override
    public OverlayContainer getOverlayContainer() {
        return component.getOverlayFragment();
    }

    @NonNull
    @Override
    public DrawerContainer getDrawerContainer() {
        return pluginView;
    }

    @NonNull
    @Override
    public ViewTarget getViewTarget() {
        return component.getViewTarget();
    }

    @NonNull
    @Override
    public AttributeTranslator getAttributeTranslator() {
        return component.getAttributeTranslator();
    }

    @NonNull
    @Override
    public MeasurementHelper getMeasurementHelper() {
        return component.getMeasurementHelper();
    }
}