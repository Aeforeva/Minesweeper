package com.example.minesweeper.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.minesweeper.databinding.CellItemBinding
import com.example.minesweeper.model.Cell

class CellAdapter(passedData: List<Cell>, private val onClick: (Cell) -> Unit) :
    RecyclerView.Adapter<CellAdapter.CellViewHolder>() {

    private val data = passedData

    class CellViewHolder(private val binding: CellItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cell: Cell) {
            binding.cell = cell
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val layout = CellItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CellViewHolder(layout)
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}