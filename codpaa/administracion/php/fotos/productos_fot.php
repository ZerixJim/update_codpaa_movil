<?
ob_start();
session_start();

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

$idFoto=$_REQUEST['idFoto'];
if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
	
	$manager = new bdManager();
	?>
	<input type="hidden" id="idFoto" name="idFoto" value="<? echo $idFoto;?>">
    	  
    <div id="det_prodFot" class="table-responsive-vertical shadow-z-1">
		<?
        $sql_prods="Select pp.idPhotoCatalogo,pp.idProducto,p.nombre,p.presentacion from photo_producto pp 
			inner join Producto p on (pp.idProducto=p.idProducto) where pp.idPhotoCatalogo='".$idFoto."'";
        $res_prods=$manager->ejecutarConsulta($sql_prods);
             
        ?>
        <table id="productos_foto" class="table table-hover table-mc-light-green">
    
            <thead>
                <tr  bgcolor="#64B5F6">
                    <th>IdPhoto</th>
                    <th>idProducto</th>
                    <th>Producto</th>
                    <th>Presentacion</th>
                </tr>
            </thead>
            <tbody>
        <?
        //************* Recorremos los productos registrados
        while($dat_prods=mysqli_fetch_array($res_prods))
        { ?>
            <tr>
                <td  data-title="IdPhoto"><? echo $dat_prods['idPhotoCatalogo'];?></td>
                <td  data-title="idProducto"><? echo $dat_prods['idProducto'];?></td>
                <td  data-title="Producto"><? echo $dat_prods['nombre'];?></td>
                <td  data-title="Presentacion"><? echo $dat_prods['presentacion'];?></td>
             </tr>
                
                <?
        }
        ?>
        </tbody>
    
     </table>
 </div>
 
 <?
	
	
}else{
	echo 'no has iniciado sesion';
	header('refresh:2,../index.php');
}

?>