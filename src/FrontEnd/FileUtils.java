package FrontEnd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.SAVE_DIALOG;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileUtils {
    
    public static String getPathFileDialog(boolean open, String filtroDescripcion, String defaultExt){
        extDefault = defaultExt;
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(filtroDescripcion, defaultExt);
        
        JFileChooser fc = getFileChooser(defaultExt);
        fc.setFileFilter(filtro);
        
        String s = (open ? "Abrir" : "Guardar como");
        fc.setDialogTitle(s);              
        
        int sel = (open ? fc.showOpenDialog(fc) : fc.showSaveDialog(fc));
        
        if (sel != JFileChooser.APPROVE_OPTION)
            return null;
        
        return fc.getSelectedFile().getAbsolutePath();
    }
    
    private static String extDefault;
    
    private static JFileChooser getFileChooser(String defaultExt){
        JFileChooser fileChooser;
        fileChooser = new JFileChooser("."){
            
            @Override
            public void approveSelection(){  //All Files
                File f = getSelectedFile();
                
                    //Poner extensión por default (sino tiene una)
                String fd = getFileFilter().getDescription();
                String ap = f.getAbsolutePath();
                
                if (!fd.equals("All Files") && !tieneExtension(ap)){
                    f = new File(ap + "." + extDefault);
                    setSelectedFile(f);
                }    
                
                if (getDialogType() != SAVE_DIALOG || !f.exists()){
                    super.approveSelection();
                    return;
                }
                
                    //Preguntar antes de sobreescribir el archivo
                int r = dialogSobreescribir(f);
                
                switch(r){
                    case JOptionPane.YES_OPTION:
                        super.approveSelection();
                        return;
                    case JOptionPane.NO_OPTION:
                        return;
                    case JOptionPane.CLOSED_OPTION:
                        return;
                    case JOptionPane.CANCEL_OPTION:
                        cancelSelection();
                        return;
                }
            
                super.approveSelection();
            }        
        };
        return fileChooser;                  
    }
    
    private static int dialogSobreescribir(File f){
        Object [] opciones ={"Si","No"};
        String m = "El archivo "+f.getName()+" ya existe.\n¿Desea sobreescribirlo?";
        return  JOptionPane.showOptionDialog(null, m, "Sobreescribir Archivo",
                                             JOptionPane.YES_NO_OPTION,
                                             JOptionPane.QUESTION_MESSAGE,null,
                                             opciones,"Si");   
    }
    
    private static boolean tieneExtension(String ruta){
       return (ruta.lastIndexOf('.') > 0); 
    }
    
    public static String getNameFile(String ruta){
        char backSlash = '\\';
        ruta = ruta.replace('/', backSlash);    //Por si se está en Linux o Mac
        
        int i = ruta.lastIndexOf(backSlash); 
        return ruta.substring(i + 1);
    }
    
    
        /**@param ruta ruta absoluta del archivo a leer.
        * @return el contenido del archivo (si no se pudo leer, devuelve null).*/
    public static String getContentFile(String ruta){   
        String result = "";
        FileReader fr = null;
        BufferedReader in = null;
        try {
            fr = new FileReader(ruta);
            in = new BufferedReader(fr);

            while(in.ready())
                result += (char) in.read();
            
            in.close();
            fr.close();
        } catch (Exception e) {
            result = null;          //El archivo no pudo leerse.
        } 
        
        return result;
    }
    
    public static boolean saveFile(String ruta, String contenido){
        try {
            FileWriter file = new FileWriter(ruta);
            file.write(contenido);
            file.close();
            return true;
        }catch(Exception e){
            return false;       //El archivo no se pudo guardar.
        }
    }
}
