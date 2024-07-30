<? 
ob_start();
session_start();

if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
?>
<html>
<head>
  
</head>

<body >
<? 
$idDoc=$_REQUEST['idDoc'];
$idProm=$_REQUEST['idProm'];
include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');
$manager = new bdManager();

$sql_doc="select * from expediente_docs where id_documento='".$idDoc."'";
$rs_doc=$manager->ejecutarConsulta($sql_doc);
$dat_doc=mysqli_fetch_array($rs_doc);

?>
<form id="form_docs" name="form_docs" method="post" action="../clasess/carga_doc_exp.php" enctype="multipart/form-data">
  <div>
  	<input type="hidden" name="idDoc" id="idDoc" value="<? echo $idDoc;?>"/>
    <input type="hidden" name="idProm" id="idProm" value="<? echo $idProm;?>"/>
  	<span><? echo "Subir ".utf8_encode($dat_doc['documento']);?></span><br>
    <input type="file" id="doc_up" name="doc_up"/>
  	 <input type="submit" value="Subir"></input>
  </div>
</form>	

   <script>
    $(function(){
            $('#form_docs').form({
				
				onSubmit:function(){
                    $.messager.progress({
                title:'Aguarde un momento',
                msg:'Cargando datos...'
            });
                },
				
                success:function(data){
					 
					 $.messager.progress('close');
					 console.log(data);
					 if(data==1)
					 {
						 $.messager.alert('Promotores','Se subio Documento con Exito','info');
						 var idDoc=$('#idDoc').val();
						 document.getElementById('doc'+idDoc).disabled=true;
						 $('#dlg_doc').dialog('close');	
						 $('#dlg_viewPm').dialog('refresh');
						 
						 }
					else
					{
							 $.messager.alert('Promotores','Ocurrio un Error al intentar subir el documento','error');	
						}
					 
								
                 }
            });
        });
    </script>   
  
</body>
</html>

<? 
}else{
	echo 'no has iniciado sesion';
	header('refresh:2,../index.php');
}

?>