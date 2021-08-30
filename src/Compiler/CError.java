package Compiler;

public class CError {
    private String errorMsj;
    private int pos;
    private String lexema;
    
    public CError(){
        init();
    }
    
    public void init(){
        errorMsj = null;
    }
    
    public void setError(String errorMsj, int pos, String lexema){
        if (hayError())
            return;         //Ya hay un error previo.
        
        if (errorMsj == null)
            errorMsj = "";
        
        this.errorMsj = errorMsj;
        this.pos      = pos;
        this.lexema   = lexema;
    }
    
    public boolean hayError(){
        return (errorMsj != null);
    }
    
    public void comunicarError(){
        onComunicarError(errorMsj, pos, lexema);
    }
    
    public void onComunicarError(String errorMsj, int pos, String lexema){ 
        //Overridable.     
    }
}
