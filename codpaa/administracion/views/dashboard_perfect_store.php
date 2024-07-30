<?php

session_start();

if(isset($_SESSION['idUser'])){ ?>
    
    <iframe title="Perfect Store Dashboard"
            width="100%"
            height="100%"
            src="https://app.powerbi.com/view?r=eyJrIjoiZGVkYmM1ZDgtYWNjOC00NWQyLTg5ODUtZDllMjk0ZjA0Zjg3IiwidCI6ImUwYmZkODJhLTI1Y2QtNGM3NS1iNjE0LWYwNTlmMDUxMDBhNiJ9"
            frameborder="0"
            allowFullScreen="true"
    </iframe>
    
<?php
    
}else{
        echo 'no has iniciado sesion';
        header('refresh:2,../index.php');
}

?>
