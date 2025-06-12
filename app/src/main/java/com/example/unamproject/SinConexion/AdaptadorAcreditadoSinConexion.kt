import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unamproject.R
import com.example.unamproject.SinConexion.AcreditadoEntity

class AdaptadorAcreditadoSinConexion(
    private val acreditados: List<AcreditadoEntity>
) : RecyclerView.Adapter<AdaptadorAcreditadoSinConexion.AcreditadoSinConexionViewHolder>() {

    inner class AcreditadoSinConexionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcreditadoSinConexionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_acreditado_trabajador_sin_conexion, parent, false)
        return AcreditadoSinConexionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AcreditadoSinConexionViewHolder, position: Int) {
        val acreditado = acreditados[position]
        val nombreCompleto = "${acreditado.id_acreditado} - " +
                "${acreditado.apellido_paterno} " +
                "${acreditado.apellido_materno} " +
                "${acreditado.nombres}"
        holder.tvNombre.text = nombreCompleto
    }

    override fun getItemCount(): Int = acreditados.size
}
