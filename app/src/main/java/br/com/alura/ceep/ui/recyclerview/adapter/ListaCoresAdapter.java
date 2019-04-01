package br.com.alura.ceep.ui.recyclerview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import br.com.alura.ceep.R;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListenerCor;

public class ListaCoresAdapter extends RecyclerView.Adapter<ListaCoresAdapter.CorViewHolder> {

    private final List<String> cores;
    private final Context context;
    private OnItemClickListenerCor onItemClickListenerCor;

    public ListaCoresAdapter(Context context, List<String> cores) {
        this.context = context;
        this.cores = cores;
    }

    @Override
    public ListaCoresAdapter.CorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.item_cor, parent, false);
        return new CorViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(ListaCoresAdapter.CorViewHolder holder, int position) {
        String cor = cores.get(position);
        holder.vincula(cor);
    }

    @Override
    public int getItemCount() {
        return cores.size();
    }

    public void setOnItemClickListenerCor(OnItemClickListenerCor onItemClickListener) {
        this.onItemClickListenerCor = onItemClickListener;
    }

    class CorViewHolder extends  RecyclerView.ViewHolder{

        String cor;
        Button btn;

        public CorViewHolder(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn_selecao_cor);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListenerCor.onItemClick(cor, getAdapterPosition());
                }
            });
        }

        public void vincula(String cor) {
            this.cor = cor;
            Drawable drawable = context.getResources().getDrawable(R.drawable.btn_shape);
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.setColor(Color.parseColor(cor));
            gradientDrawable.setShape(GradientDrawable.OVAL);
            btn.setBackground(drawable);
        }

    }
}
