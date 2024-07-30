<?php
ob_start();

session_start();
include_once('../../php/seguridad.php');
$idPromotor=$_REQUEST['idPromotor'];
$fecha=$_REQUEST['fecha'];

?>


<div id="rastreo" style=" height:100%">

<input type="hidden" name="idPromoP" id="idPromoP" value="<? echo $idPromotor;?>"/>
<input type="hidden" name="fechaP" id="fechaP" value="<? echo $fecha;?>"/>

    <!--Rastreo -->

    <? if($_SESSION['permiso'] >= 1){ ?>

        <div title="Rastreo_prod" style=" height:100%">
			<div><h4> Dia: <? echo $fecha;?></h4> </div>
    
                    <div id="Mapa-rastreoP"></div>


        </div>

<script>

    carga_mapa();

</script>        
      <? }else{} ?>

    <!-- Fin RASTREO-->



</div>