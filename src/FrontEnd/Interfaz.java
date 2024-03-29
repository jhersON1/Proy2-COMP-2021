package FrontEnd;

import Compiler.Compilador;
import java.awt.Color;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter.HighlightPainter;

public class Interfaz extends javax.swing.JFrame {
    private Compilador compilador;
   
    
    public Interfaz() {
        initComponents();
        
        nuevoDoc();      //Iniciar nuevo documento.
        setEventosModificacion(textArea);       
                     
        compilador = new Compilador(){
            @Override
            public void onComunicarError(String errorMsj, int pos, String lexema){
                if (hayError()){
                    labelError.setForeground(Color.red);
                    labelError.setText(errorMsj);
                    resaltar(pos, lexema.length());
                }
                else{
                    labelError.setForeground(Color.BLUE);
                    labelError.setText("No se encontraron errores");
                }    
            }
        };
        
    }

//----------------------------------
    private void resaltar(int pos, int longitud){
        desResaltar();
        HighlightPainter colorResaltado = new DefaultHighlighter.DefaultHighlightPainter(Color.PINK);
        
        try {
           textArea.getHighlighter().addHighlight(pos, pos+longitud, colorResaltado); 
        } catch (Exception e) {}
        
        textArea.setSelectionStart(pos);
        textArea.setSelectionEnd(pos + longitud);
    }
    
    private void desResaltar(){
        textArea.getHighlighter().removeAllHighlights();
    }
//----------------------------------
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        scrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        labelError = new java.awt.Label();
        MenuBar = new javax.swing.JMenuBar();
        mArchivo = new javax.swing.JMenu();
        itmNuevo = new javax.swing.JMenuItem();
        itmAbrir = new javax.swing.JMenuItem();
        itmGuardar = new javax.swing.JMenuItem();
        itmGuardarComo = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        itmSalir = new javax.swing.JMenuItem();
        Compilador = new javax.swing.JMenu();
        Compilar = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        jMenu2.setText("jMenu2");

        jMenu3.setText("jMenu3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));
        setSize(new java.awt.Dimension(640, 480));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        scrollPane.setBorder(null);
        scrollPane.setFont(new java.awt.Font("Courier New", 0, 13)); // NOI18N

        textArea.setColumns(20);
        textArea.setFont(new java.awt.Font("Courier New", 0, 13)); // NOI18N
        textArea.setRows(5);
        textArea.setTabSize(2);
        textArea.setToolTipText("");
        scrollPane.setViewportView(textArea);

        labelError.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        labelError.setForeground(new java.awt.Color(0, 0, 255));

        mArchivo.setText("Archivo");

        itmNuevo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itmNuevo.setText("Nuevo");
        itmNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmNuevoActionPerformed(evt);
            }
        });
        mArchivo.add(itmNuevo);

        itmAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itmAbrir.setText("Abrir...");
        itmAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmAbrirActionPerformed(evt);
            }
        });
        mArchivo.add(itmAbrir);

        itmGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itmGuardar.setText("Guardar");
        itmGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmGuardarActionPerformed(evt);
            }
        });
        mArchivo.add(itmGuardar);

        itmGuardarComo.setText("Guardar como...");
        itmGuardarComo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmGuardarComoClick(evt);
            }
        });
        mArchivo.add(itmGuardarComo);
        mArchivo.add(jSeparator1);

        itmSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itmSalir.setText("Salir");
        itmSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmSalirActionPerformed(evt);
            }
        });
        mArchivo.add(itmSalir);

        MenuBar.add(mArchivo);

        Compilador.setText("Compilador");

        Compilar.setText("Compilar");
        Compilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CompilarActionPerformed(evt);
            }
        });
        Compilador.add(Compilar);

        MenuBar.add(Compilador);

        setJMenuBar(MenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelError, javax.swing.GroupLayout.PREFERRED_SIZE, 575, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(215, Short.MAX_VALUE))
            .addComponent(scrollPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelError, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        labelError.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void itmAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmAbrirActionPerformed
        String ruta = FileUtils.getPathFileDialog(true, DESCRIPCIONfile, DEFAULText);
        
        if (ruta == null)
            return;
        
        if (!ruta.equals(path))
            confirmarGuardarDocDialog();
        
        String content = FileUtils.getContentFile(ruta);      
        if (content != null)
            setPathContenido(ruta, content);    //Cargar el textArea con el content. 
    }//GEN-LAST:event_itmAbrirActionPerformed

    private void itmGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmGuardarActionPerformed
        if (path == null)
            itmGuardarComoClick(evt);     //Hacer un "Guardar como..."
        else
           guardarDoc(path);
    }//GEN-LAST:event_itmGuardarActionPerformed

    private void itmGuardarComoClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmGuardarComoClick
        String ruta = FileUtils.getPathFileDialog(false, DESCRIPCIONfile, DEFAULText);     
        guardarDoc(ruta);
    }//GEN-LAST:event_itmGuardarComoClick

    
    private void itmNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmNuevoActionPerformed
        confirmarGuardarDocDialog();
        nuevoDoc();
    }//GEN-LAST:event_itmNuevoActionPerformed

    private void itmSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmSalirActionPerformed
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_itmSalirActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        confirmarGuardarDocDialog();
    }//GEN-LAST:event_formWindowClosing

    private void CompilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CompilarActionPerformed
       compilador.compilar(textArea.getText());
       compilador.comunicarError();     //fire onComunicarError (ver el constructor de esta class)
    }//GEN-LAST:event_CompilarActionPerformed
   
//------------------------ Manejo del Documento --------------------------------    
    private static final String DESCRIPCIONfile = "Archivos de texto (*.txt)";
    private static final String DEFAULText      = "txt";
    
    private String path;    //Ruta absoluta del archivo que maneja el TextArea.
    private String fileName;
    private String contenido;   //Última modificación del contenido del textArea. 
    
    private void setCaption(){
        String asterisco=(docModificado() ? "*" : "");          
        String fn = (path == null ? "(Sin Nombre)" : fileName);
        this.setTitle(asterisco + fn);    
    }
    
    private void nuevoDoc(){
        setPathContenido(null, "");
    }
    
    private void setPathContenido(String ruta, String content){
        setContenido(content);
        setPath(ruta);
    }
    
    private void setPath(String ruta){
        path = ruta;
        
        if (path != null)            
            fileName = FileUtils.getNameFile(path);
        
        setCaption();
    }
    
    private void setContenido(String s){
        contenido = s;
        textArea.setText(contenido);
    }   
    
    private boolean docModificado(){
       return !contenido.equals( textArea.getText() ); 
    }
    
    private void confirmarGuardarDocDialog(){
        if (!docModificado())
            return;
        
        String name = (path==null ? "" : FileUtils.getNameFile(path)+", ");
        String msj = "El documento "+name+"ha sido modificado\n¿Desea guardar los cambios?";
        
        int n = JOptionPane.showConfirmDialog(null, msj, "Guardar", JOptionPane.YES_NO_OPTION);
        
        if (n==0)
            itmGuardarActionPerformed(null);
    }
    
    private void guardarDoc(String ruta){
        if (ruta == null)
            return;
        
        String content = textArea.getText();
        
        if ( FileUtils.saveFile(ruta, content) ){
            contenido = content;
            setPath(ruta);
        }            
    }
    
    private void setEventosModificacion(JTextArea textArea){
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                onModificacion();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                onModificacion();
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
                onModificacion();
            }
        });
    }
    
    private void onModificacion(){
        setCaption();
        labelError.setText("");
        desResaltar();
    }
    
    
    /**@param args the command line arguments*/
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Interfaz form = new Interfaz();
                //new Interfaz().setVisible(true);
                form.setLocationRelativeTo(null);  //Centrar el form
                form.setVisible(true);
            }
        });     
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Compilador;
    private javax.swing.JMenuItem Compilar;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JMenuItem itmAbrir;
    private javax.swing.JMenuItem itmGuardar;
    private javax.swing.JMenuItem itmGuardarComo;
    private javax.swing.JMenuItem itmNuevo;
    private javax.swing.JMenuItem itmSalir;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private java.awt.Label labelError;
    private javax.swing.JMenu mArchivo;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables
}
