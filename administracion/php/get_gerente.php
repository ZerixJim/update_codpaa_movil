<?
include_once('../connexion/bdManager.php');

	$manager = new bdManager();
	$rs = $manager->ejecutarConsulta('select idGerente,nombre from Gerentes order by nombre asc');
    $result = array();  
    while($row = mysqli_fetch_object($rs)){  
	
		$row->razonsocial=utf8_encode($row->razonsocial);
        array_push($result, $row);  
    }  
	  
    echo json_encode($result,JSON_PARTIAL_OUTPUT_ON_ERROR);  

?>