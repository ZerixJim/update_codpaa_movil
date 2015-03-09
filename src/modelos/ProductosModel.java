package modelos;

/**
 * Created by Gustavo Ramón Ibarra Maciel on 04/12/2014.
 */
public class ProductosModel {
    private int _idProducto;
    private String _nombre;
    private String _presentacion;
    private boolean seleted = false;

    public boolean isSeleted() {
        return seleted;
    }

    public void setSeleted(boolean seleted) {
        this.seleted = seleted;
    }

    public void setIdProducto(int idProductos){
        this._idProducto = idProductos;
    }

    public void setNombre(String nombre){
        this._nombre = nombre;
    }

    public void setPresentacion(String presentacion){
        this._presentacion = presentacion;
    }

    public int getIdProducto(){
        return this._idProducto;
    }
    public String getNombre(){
        return this._nombre;
    }

    public String getPresentacion(){
        return this._presentacion;
    }
}
