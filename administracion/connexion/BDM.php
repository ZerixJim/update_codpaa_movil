<?

require('config.php');

class BDM {
    
    public function ejecutarConsulta($sql){
        $con = mysql_connect(configuracion::getBDServer_codpaa(),configuracion::getBDUsuario(),configuracion::getBDPass());
        if(!$con){
            die('Error al conectarse: '.mysql_error());
        }
        mysql_select_db(configuracion::getBDNombre(), $con);
        
        $result = mysql_query($sql);
        mysql_close($con);
        
        return $result;
    }
    
    
}










?>