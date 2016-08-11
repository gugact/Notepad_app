package br.usp.icmc.gustavo.notepad_app.model;

/**
 * Created by Gustavo on 05/07/2016.
 */

import android.content.Context;
import android.widget.ArrayAdapter;

import br.usp.icmc.gustavo.notepad_app.R;

public class CustomAdapter extends ArrayAdapter{
    private Model[] modelItems = null;
    private Context context;

    public CustomAdapter(Context context, Model[] resource) {
        super(context, R.layout.listview_delete_items_layout,resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.modelItems = resource;
        }

    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.row, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.textView1);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
        name.setText(modelItems[position].getName());
        if(modelItems[position].getValue() == 1)
        cb.setChecked(true);
        else
        cb.setChecked(false);
        return convertView;
        }*/

        }
