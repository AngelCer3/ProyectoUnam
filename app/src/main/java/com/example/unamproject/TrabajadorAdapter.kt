import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unamproject.EditarTrabajadorActivity
import com.example.unamproject.R
import com.example.unamproject.TrabajadorResponse

class TrabajadorAdapter(
    private val listaTrabajadores: List<TrabajadorResponse>
) : RecyclerView.Adapter<TrabajadorAdapter.TrabajadorViewHolder>() {

    class TrabajadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtCorreo: TextView = itemView.findViewById(R.id.txtCorreo)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrabajadorViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trabajador, parent, false)

        return TrabajadorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaTrabajadores.size
    }

    override fun onBindViewHolder(holder: TrabajadorViewHolder, position: Int) {

        val trabajador = listaTrabajadores[position]

        holder.txtCorreo.text = trabajador.correo

        holder.btnEditar.setOnClickListener {

            val context = holder.itemView.context

            val intent = Intent(context, EditarTrabajadorActivity::class.java)

            intent.putExtra("id_usuario", trabajador.id)

            context.startActivity(intent)

        }

    }
}