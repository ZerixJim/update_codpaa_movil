<?php
/**
 * Header file
*/
use PhpOffice\PhpPresentation\Autoloader;
use PhpOffice\PhpPresentation\Settings;
use PhpOffice\PhpPresentation\IOFactory;
use PhpOffice\PhpPresentation\Slide;
use PhpOffice\PhpPresentation\PhpPresentation;
use PhpOffice\PhpPresentation\AbstractShape;
use PhpOffice\PhpPresentation\DocumentLayout;
use PhpOffice\PhpPresentation\Shape\Drawing;
use PhpOffice\PhpPresentation\Shape\MemoryDrawing;
use PhpOffice\PhpPresentation\Shape\RichText;
use PhpOffice\PhpPresentation\Shape\RichText\BreakElement;
use PhpOffice\PhpPresentation\Shape\RichText\TextElement;
use PhpOffice\PhpPresentation\Style\Alignment;
use PhpOffice\PhpPresentation\Style\Bullet;
use PhpOffice\PhpPresentation\Style\Color;

error_reporting(E_ALL);
define('CLI', (PHP_SAPI == 'cli') ? true : false);
define('EOL', CLI ? PHP_EOL : '<br />');
define('SCRIPT_FILENAME', basename($_SERVER['SCRIPT_FILENAME'], '.php'));
define('IS_INDEX', SCRIPT_FILENAME == 'index');

require_once __DIR__ . '/../clasess/PhpPresentation/Autoloader.php';
Autoloader::register();

require_once __DIR__ . '/../clasess/Common/Autoloader.php';
\PhpOffice\Common\Autoloader::register();

//require_once __DIR__ . '/../vendor/autoload.php';

// Set writers
$writers = array('PowerPoint2007' => 'pptx', 'ODPresentation' => 'odp');

// Return to the caller script when runs by CLI
if (CLI) {
    return;
}

// Set titles and names
$pageHeading = str_replace('_', ' ', SCRIPT_FILENAME);
$pageTitle = IS_INDEX ? 'Welcome to ' : "{$pageHeading} - ";
$pageTitle .= 'PHPPresentation';
$pageHeading = IS_INDEX ? '' : "<h1>{$pageHeading}</h1>";

/*$oShapeDrawing = new Drawing\File();
$oShapeDrawing->setName('PHPPresentation logo')
    ->setDescription('PHPPresentation logo')
    ->setPath('../resources/phppowerpoint_logo.gif')
    ->setHeight(36)
    ->setOffsetX(10)
    ->setOffsetY(10);
$oShapeDrawing->getShadow()->setVisible(true)
    ->setDirection(45)
    ->setDistance(10);
$oShapeDrawing->getHyperlink()->setUrl('https://github.com/PHPOffice/PHPPresentation/')->setTooltip('PHPPresentation');*/

// Create a shape (text)
$oShapeRichText = new RichText();
$oShapeRichText->setHeight(300)
    ->setWidth(600)
    ->setOffsetX(170)
    ->setOffsetY(180);
$oShapeRichText->getActiveParagraph()->getAlignment()->setHorizontal( Alignment::HORIZONTAL_CENTER );
$textRun = $oShapeRichText->createTextRun('Thank you for using PHPPresentation!');
$textRun->getFont()->setBold(true)
    ->setSize(60)
    ->setColor( new Color( 'FFE06B20' ) );



// Populate samples
$files = '';
if ($handle = opendir('.')) {
    while (false !== ($file = readdir($handle))) {
        if (preg_match('/^Sample_\d+_/', $file)) {
            $name = str_replace('_', ' ', preg_replace('/(Sample_|\.php)/', '', $file));
            $files .= "<li><a href='{$file}'>{$name}</a></li>";
        }
    }
    closedir($handle);
}

/**
 * Write documents
 *
 * @param \PhpOffice\PhpPresentation\PhpPresentation $phpPresentation
 * @param string $filename
 * @param array $writers
 */
function write($phpPresentation, $filename, $writers)
{
    $result = '';
    
    // Write documents
    foreach ($writers as $writer => $extension) {

        if (!is_null($extension)) {

            $path = dirname(__DIR__ . "../") . "/archivos/pptxreporter" . "/{$filename}.{$extension}";

            $xmlWriter = IOFactory::createWriter($phpPresentation, $writer);
            $xmlWriter->save($path);
            //rename(__DIR__ . "/{$filename}.{$extension}", "../archivos/{$filename}.{$extension}");

            $result = $_SERVER['HTTP_HOST'] . '/archivos/pptxreporter/' .  basename($path);


        } else {
            $result = -1;
        }
       
    }

   // $result .= getEndingNotes($writers);

    return $result;
}

/**
 * Get ending notes
 *
 * @param array $writers
 */
function getEndingNotes($writers)
{
    $result = '';

    // Do not show execution time for index
    if (!IS_INDEX) {
        $result .= date('H:i:s') . " Done writing file(s)" . EOL;
        $result .= date('H:i:s') . " Peak memory usage: " . (memory_get_peak_usage(true) / 1024 / 1024) . " MB" . EOL;
    }

    // Return
    if (CLI) {
        $result .= 'The results are stored in the "results" subdirectory.' . EOL;
    } else {
        if (!IS_INDEX) {
            $types = array_values($writers);
            $result .= '<p>&nbsp;</p>';
            $result .= '<p>Results: ';
            foreach ($types as $type) {
                if (!is_null($type)) {
                    $resultFile = 'results/' . SCRIPT_FILENAME . '.' . $type;
                    if (file_exists($resultFile)) {
                        $result .= "<a href='{$resultFile}' class='btn btn-primary'>{$type}</a> ";
                    }
                }
            }
            $result .= '</p>';
        }
    }

    return $result;
}

/**
 * Creates a templated slide
 *
 * @param PHPPresentation $objPHPPresentation
 * @return \PhpOffice\PhpPresentation\Slide
 */
function createTemplatedSlide(PhpOffice\PhpPresentation\PhpPresentation $objPHPPresentation)
{
    // Create slide
    $slide = $objPHPPresentation->createSlide();
    
    // Add logo
    /*  $shape = $slide->createDrawingShape();
  $shape->setName('PHPPresentation logo')
        ->setDescription('PHPPresentation logo')
        ->setPath('./resources/phppowerpoint_logo.gif')
        ->setHeight(36)
        ->setOffsetX(10)
        ->setOffsetY(10);
    $shape->getShadow()->setVisible(true)
        ->setDirection(45)
        ->setDistance(10);*/

    // Return slide
    return $slide;
}

?>
