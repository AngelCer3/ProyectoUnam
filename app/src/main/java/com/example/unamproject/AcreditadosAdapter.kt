import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unamproject.R
import com.example.unamproject.identificarAcreditado

class AcreditadosAdapter(
    private val acreditados: List<identificarAcreditado>,
    private val onUpdateClick: (identificarAcreditado) -> Unit
) : RecyclerView.Adapter<AcreditadosAdapter.AcreditadoViewHolder>() {

    inner class AcreditadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val btnActualizar: Button = itemView.findViewById(R.id.btnActualizar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcreditadoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_acreditado, parent, false)
        return AcreditadoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AcreditadoViewHolder, position: Int) {
        val acreditado = acreditados[position]

        // Formatear el texto como "ID - ApellidoPaterno ApellidoMaterno Nombres"
        val nombreCompleto = "${acreditado.id_acreditado} - " +
                "${acreditado.apellido_paterno} " +
                "${acreditado.apellido_materno} " +
                "${acreditado.nombres}"

        holder.tvNombre.text = nombreCompleto

        holder.btnActualizar.setOnClickListener {
            onUpdateClick(acreditado)
        }
    }

    override fun getItemCount(): Int = acreditados.size
}