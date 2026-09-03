package com.example.tecnomarketapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private List<Producto> lista;

    public ProductoAdapter(List<Producto> lista) {
        this.lista = lista;
    }

    public void actualizarLista(List<Producto> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto p = lista.get(position);
        holder.tvNombre.setText(p.getNombre());
        holder.tvCodigo.setText("Cód: " + p.getCodigo());
        holder.tvCategoria.setText("Categoría: " + p.getCategoria());
        holder.tvPrecio.setText(String.format(Locale.getDefault(), "S/ %.2f", p.getPrecio()));
        holder.tvStock.setText("Stock: " + p.getStock());

        // Evento de clic en la tarjeta
        holder.itemView.setOnClickListener(v -> {
            Context ctx = v.getContext();
            Intent intent = new Intent(ctx, DetalleProductoActivity.class);
            intent.putExtra("producto", p);
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCodigo, tvCategoria, tvPrecio, tvStock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvCardNombre);
            tvCodigo = itemView.findViewById(R.id.tvCardCodigo);
            tvCategoria = itemView.findViewById(R.id.tvCardCategoria);
            tvPrecio = itemView.findViewById(R.id.tvCardPrecio);
            tvStock = itemView.findViewById(R.id.tvCardStock);
        }
    }
}