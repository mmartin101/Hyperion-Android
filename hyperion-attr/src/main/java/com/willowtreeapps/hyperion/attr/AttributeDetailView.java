package com.willowtreeapps.hyperion.attr;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.willowtreeapps.hyperion.core.ViewTarget;
import com.willowtreeapps.hyperion.core.plugins.ExtensionProvider;
import com.willowtreeapps.hyperion.core.plugins.PluginExtension;

import java.util.ArrayList;
import java.util.List;

class AttributeDetailView extends RecyclerView implements ViewTarget.Observer {

    static final int ITEM_HEADER = 1;
    static final int ITEM_ATTRIBUTE = 2;

    private final ViewTarget target;
    private final AttributeAdapter adapter;
    private final AttributeLoader attributeLoader;

    public AttributeDetailView(@NonNull Context context) {
        this(context, null);
    }

    public AttributeDetailView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AttributeDetailView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        PluginExtension extension = ExtensionProvider.get(context);
        target = extension.getViewTarget();
        adapter = new AttributeAdapter();
        attributeLoader = new AttributeLoader(extension.getAttributeTranslator());

        setHasFixedSize(true);
        setLayoutManager(new LinearLayoutManager(context));
        setAdapter(adapter);
        addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        target.registerObserver(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        target.unregisterObserver(this);
    }

    @Override
    public void onTargetChanged(View target) {
        List<Section<ViewAttribute>> sections = attributeLoader.getAttributesForView(target);
        List<AttributeDetailItem> items = toItems(sections);
        adapter.setItems(items);
    }

    private List<AttributeDetailItem> toItems(List<Section<ViewAttribute>> sections) {
        List<AttributeDetailItem> items = new ArrayList<>(18);
        for (Section<ViewAttribute> section : sections) {
            items.add(new Header(section.getName()));
            items.addAll(section.getList());
        }
        return items;
    }

    private static class AttributeAdapter extends RecyclerView.Adapter<DataViewHolder> {

        private List<AttributeDetailItem> items;

        private void setItems(List<AttributeDetailItem> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView;
            switch (viewType) {
                case ITEM_ATTRIBUTE:
                    itemView = inflater.inflate(R.layout.ha_item_attribute, parent, false);
                    return new AttributeViewHolder(itemView);
                case ITEM_HEADER:
                    itemView = inflater.inflate(R.layout.ha_item_header, parent, false);
                    return new HeaderViewHolder(itemView);
                default:
                    throw new IllegalStateException("Did not recognize view type: " + viewType);
            }
        }

        @Override
        public void onBindViewHolder(DataViewHolder holder, int position) {
            //noinspection unchecked
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position).getViewType();
        }
    }

    private static class AttributeViewHolder extends DataViewHolder<ViewAttribute> {

        private final TextView keyText;
        private final TextView valueText;
        private final ImageView image;

        private AttributeViewHolder(View itemView) {
            super(itemView);
            keyText = (TextView) itemView.findViewById(R.id.key_text);
            valueText = (TextView) itemView.findViewById(R.id.value_text);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

        @Override
        void onDataChanged(ViewAttribute data) {
            keyText.setText(data.getKey());

            Object value = data.getValue();
            if (value != null) {
                valueText.setText(value.toString());
                valueText.setVisibility(View.VISIBLE);
            } else {
                valueText.setVisibility(View.INVISIBLE);
            }

            Drawable drawable = data.getDrawable();
            if (drawable != null) {
                image.setImageDrawable(drawable);
                image.setVisibility(View.VISIBLE);
            } else {
                image.setVisibility(View.GONE);
            }
        }
    }

    private static class HeaderViewHolder extends DataViewHolder<Header> {

        private final TextView text;

        private HeaderViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }

        @Override
        void onDataChanged(Header data) {
            text.setText(data.getText());
        }
    }
}