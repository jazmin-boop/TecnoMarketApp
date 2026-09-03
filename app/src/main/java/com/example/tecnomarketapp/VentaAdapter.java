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

public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.ViewHolder> {

    private List<Venta> lista;

    public VentaAdapter(List<Venta> lista) {
        this.lista = lista;
    }

    public void actualizarLista(List<Venta> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Venta v = lista.get(position);
        Context ctx = holder.itemView.getContext();

        holder.tvCliente.setText(v.getCliente());
        holder.tvFecha.setText(v.getFecha());
        holder.tvIdVenta.setText("#VEN-" + String.format(Locale.US, "%03d", v.getIdVenta()));
        holder.tvVendedor.setText("Atendido por: " + v.getVendedor());
        holder.tvTotal.setText(String.format(Locale.US, "S/ %.2f", v.getTotal()));

        // Al presionar la tarjeta de venta, abre su pantalla de detalle showcase
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(ctx, DetalleVentaActivity.class);
            intent.putExtra("idVenta", v.getIdVenta());
            intent.putExtra("cliente", v.getCliente());
            intent.putExtra("vendedor", v.getVendedor());
            intent.putExtra("fecha", v.getFecha());
            intent.putExtra("subtotal", v.getSubtotal());
            intent.putExtra("igv", v.getIgv());
            intent.putExtra("total", v.getTotal());
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCliente, tvFecha, tvIdVenta, tvVendedor, tvTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCliente = itemView.findViewById(R.id.tvCardCliente);
            tvFecha = itemView.findViewById(R.id.tvCardFecha);
            tvIdVenta = itemView.findViewById(R.id.tvCardIdVenta);
            tvVendedor = itemView.findViewById(R.id.tvCardVendedor);
            tvTotal = itemView.findViewById(R.id.tvCardTotal);
        }
    }
}