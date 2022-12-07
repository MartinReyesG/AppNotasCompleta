package com.bersyte.noteapp.adaptador

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bersyte.noteapp.databinding.AdaptadorNotasBinding
import com.bersyte.noteapp.fragmentos.FGInicioDirections
import com.bersyte.noteapp.model.Note
import java.util.*

//Los adaptadores se encargan de mostrar el contenido de las tareas o de las notas
//Es un enlace entre los datos (modelo) y las vistas (en este caso el contenido de las tarjetas)
class AdaptadorNotas : RecyclerView.Adapter<AdaptadorNotas.NoteViewHolder>() {

    class NoteViewHolder(val itemBinding: AdaptadorNotasBinding) : //itemBinding es el encargado de acceder a los ID de la vista de la nota
        RecyclerView.ViewHolder(itemBinding.root) //pasa la vista raiz del enlace pasado (adaptador_notas.xml)


    private val differCallback = //informa como calcular las actualizaciones de la lista
        //es como un foreach que recorre el contenido de las notas o tareas y las va asignando
        //El adaptador actualiza los elementos (las cards).
        object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id && //id de la nota
                        oldItem.noteBody == newItem.noteBody &&
                        oldItem.notetvDate == newItem.notetvDate &&
                        oldItem.noteSubTitle == newItem.noteSubTitle &&
                        oldItem.noteTitle == newItem.noteTitle
            }

            //determinaa los cambios estructurales entre la lista antigua y la nueva
            // (adiciones / eliminaciones / cambios de posición)
            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }

    //crea los datos que irán en la vista inflando el xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            AdaptadorNotasBinding.inflate( //infla el AdaptadorNotas.xml que corresponde a un layout
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }
    val differ = AsyncListDiffer(this, differCallback) //

    //presenta los datos que irán en la vista
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position] //accede a la nota actual iterando entre notas

        holder.itemBinding.tvNoteTitle.text = currentNote.noteTitle //asigna el model Note al item del layout
        holder.itemBinding.tvNoteSubTitle.text = currentNote.noteSubTitle
        holder.itemBinding.tvNoteDate.text = currentNote.notetvDate
        holder.itemBinding.tvNoteBody.text = currentNote.noteBody

        holder.itemView.setOnClickListener { view ->
            val direction = FGInicioDirections
                .actionHomeFragmentToUpdateNoteFragment(currentNote)
            view.findNavController().navigate(direction)
        }
    }

    //muestra el tamaño de la lista
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}