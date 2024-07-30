<?
ob_start();
session_start();

if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
    
    $tipo = $_SESSION['tipo'];
    
?>

<!DOCTYPE html>
<html>
    <head>
        <title>Catalogo codpaa</title>
        <meta charset="utf-8">
        <link rel="stylesheet" type="text/css" href="../themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="../themes/icon.css">
        <link rel="stylesheet" type="text/css" href="../css/estilocatalogo.css"></link>
	<link rel="icon" href="../imagenes/favicon.ico">
        
	<script type="text/javascript" src="../jquery.min.js"></script>
	<script type="text/javascript" src="../jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../easyloader.js"></script>
	<script type="text/javascript" src="../script/bufferview.js"></script>
	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCDb3AWOIzMkm-_tdCezft9_ygxrgZ3__0&sensor=false"></script>
	<script type="text/javascript" src="../script/scriptcata.js"></script>
	<script type="text/javascript" src="../script/jquery.exif.js"></script>
    </head>
    
    
    <body>
        
        <header></header>
        <div id="contenedor">
	    
            
            
            
            
            
            
            
        </div>
        
    </body>
    
    
    
</html>




    
<?    
}else{
	echo 'No has iniciado sesion, redireccionando.....';
	header('refresh:2,../index.php');
}
    








?>