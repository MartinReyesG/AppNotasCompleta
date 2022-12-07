package com.bersyte.noteapp.adaptador

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bersyte.noteapp.databinding.AdaptadorNotasBinding
import com.bersyte.noteapp.databinding.AdaptadorTareasBinding
import com.bersyte.noteapp.fragmentos.FGInicioDirections
import com.bersyte.noteapp.model.Note
import com.bersyte.noteapp.model.Tarea
import java.util.*


//Los adaptadores se encargan de mostrar el contenido de las tareas o de las notas
//Es un enlace entre los datos (modelo) y las vistas (en este caso el contenido de las tarjetas)
class AdaptadorTareas : RecyclerView.Adapter<AdaptadorTareas.TareaViewHolder>() {

    class TareaViewHolder(val itemBinding: AdaptadorTareasBinding) ://itemBinding es el encargado de acceder a los ID de la vista de la tarea
        RecyclerView.ViewHolder(itemBinding.root)//pasa la vista raiz del enlace pasado (adaptador_tareas.xml)


    private val differCallback = //informa como calcular las actualizaciones de la lista
        //es como un foreach que recorre el contenido de las notas o tareas y las va asignando
        //El adaptador actualiza los elementos (las cards).
        object : DiffUtil.ItemCallback<Tarea>() {
            override fun areItemsTheSame(oldItem: Tarea, newItem: Tarea): Boolean {
                return oldItem.id == newItem.id && //id de la tarea
                        oldItem.tareaBody == newItem.tareaBody &&
                        oldItem.tareatvDate == newItem.tareatvDate &&
                        oldItem.tareaSubTitle == newItem.tareaSubTitle &&
                        oldItem.tareaTitle == newItem.tareaTitle
            }
            //determinaa los cambios estructurales entre la lista antigua y la nueva
            // (adiciones / eliminaciones / cambios de posición)
            override fun areContentsTheSame(oldItem: Tarea, newItem: Tarea): Boolean {
                return oldItem == newItem
            }

        }

    val differ = AsyncListDiffer(this, differCallback)
    //crea los datos que irán en la vista inflando el xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        return TareaViewHolder(
            AdaptadorTareasBinding.inflate(  //infla el AdaptadorTareas.xml que corresponde a un layout
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }
    //presenta los datos que irán en la vista
    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val currentTarea = differ.currentList[position] //accede a la tarea actual iterando entre tareas


        holder.itemBinding.tvTareaTitle.text = currentTarea.tareaTitle//asigna el model Note al item del layout
        holder.itemBinding.tvTareaSubTitle.text = currentTarea.tareaSubTitle
        holder.itemBinding.tvTareaDate.text = currentTarea.tareatvDate
        holder.itemBinding.tvTareaBody.text = currentTarea.tareaBody

        holder.itemView.setOnClickListener { view ->

            val direction = FGInicioDirections
                .actionHomeFragmentToUpdateTareaFragment(currentTarea)
            view.findNavController().navigate(direction) //nos manda a la pantalla de actualizar tarea
        }
    }
    //muestra el tamaño de la lista
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}