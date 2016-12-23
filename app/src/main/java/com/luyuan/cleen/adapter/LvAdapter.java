package com.luyuan.cleen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luyuan.cleen.R;
import com.luyuan.cleen.Utils.ImageDownLoader;
import com.luyuan.cleen.model.CleenHero;


/**
 * Author: Leaf
 * Date：2016/12/20
 */

public class LvAdapter extends BaseAdapter {
    private Context context;
    private CleenHero cleenHero;
    public LvAdapter(Context context,CleenHero cleenHero) {
        this.context = context;
        this.cleenHero = cleenHero;
    }

    @Override
    public int getCount() {
        return cleenHero.getRows().size();
    }

    @Override
    public Object getItem(int position) {
        return cleenHero.getRows().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_layout,null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.description = (TextView) convertView.findViewById(R.id.tv_description);
            viewHolder.imageHref = (ImageView) convertView.findViewById(R.id.iv_imageHref);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if( cleenHero.getRows().get(position) != null){
            if(cleenHero.getRows().get(position).getTitle() !=null){
                viewHolder.title.setText(cleenHero.getRows().get(position).getTitle());
            }else{
                viewHolder.title.setText("null");
            }
            if(cleenHero.getRows().get(position).getDescription() != null){
                viewHolder.description.setText(cleenHero.getRows().get(position).getDescription());
            }else{
                viewHolder.description.setText("null");
            }

            String url = cleenHero.getRows().get(position).getImageHref();
            ImageDownLoader.get(context).display(url,R.mipmap.error,viewHolder.imageHref);
            //viewHolder.imageHref.setImageURI(Uri.parse(cleenHero.getRows().get(position).getImageHref()));
        }
        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView description;
        ImageView imageHref;
    }
}
