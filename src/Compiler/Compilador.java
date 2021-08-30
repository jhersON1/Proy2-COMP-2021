package Compiler;

public class Compilador {
    public CError error;
    public Analex analex;
    private Parser parser;
    
    public Compilador(){
        error = new CError(){
            @Override
            public void onComunicarError(String errorMsj, int pos, String lexema){
                Compilador.this.onComunicarError(errorMsj, pos, lexema);
            }
        };
        
        analex = new Analex(error);
        parser = new Parser(analex, error);
    }
    
    public void compilar(String progFuente){
        error.init();
        analex.init(progFuente);
        
        parser.programa();      //Llamar al símbolo inicial de la BNF.        
        comunicarError();
    }
    
    public void comunicarError(){
        error.comunicarError();
    }
    
    public boolean hayError(){
        return error.hayError();
    }
    
    public void onComunicarError(String errorMsj, int pos, String lexema){ 
        //Overridable
    }
}
