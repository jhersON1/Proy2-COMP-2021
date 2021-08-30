package Compiler;

public class Analex {   //Es una cinta de tokens
    private Cinta M;
    private CError error; 
    
    private Token R;        //Result del dt (Preanalisis)
    private String ac;
    private int pos;        //Posición de inicio del lexema del preanalisis().
    
    public Analex(CError error){
        M = new Cinta();
        this.error = error;
        
        R = new Token();
        init();
    }
    
    public final void init(){
       M.init();
       avanzar();      //Calcular el primer preanalisis.
    }
    
    public final void init(String progFuente){
        M.init(progFuente);
        init();
    }
    
    public Token preanalisis(){
        return R;
    }
    
    public String lexema(){
        return ac;
    }
    
    public void avanzar(){
       dt();
    }
    
    public int getPos(){    //Devuelve la posición de inicio de lexema()
        return pos;
    }
    
    private void dt(){   
        int nom;
        int estado = 0;
        ac = ""; 
        
        while (true){
            char c = M.cc();
           
            switch (estado){
                case 0  :   
                    avEspacios(M);      //"Quitar" Espacios iniciales
                    pos = M.getPos();
                    c = M.cc();

                    if (c == Cinta.EOF){
                        R.setNom(Token.FIN);
                        return;
                    } 

                    estado = divOrComment();
                    if (estado != -1)
                        break;  //Se secuenció DIV o comentario  //...
                   
                    if ((estado = paOrComment()) != -1)
                        break;  //Se secuenció PA o commentario (*..*)
                    
                    if ((estado = commentLlave()) != -1)
                        break;  //Se secuenció commentario {..}
                    
                    if ((estado = dosPuntosOrAssign()) != -1)
                        break;  //Se reconoció <DOSPTOS, x> o <ASSIGN, _>
                    
                    if ((estado = oprelOrNot()) != -1)
                        break;  //Se reconoció <OPREL, x> o <NOT, _>

                    ac = ""+c;                         

                    if (Character.isDigit(c)){  //NUM                                
                        M.avanzar();    estado=10;
                        break;
                    }

                    if (letra(c)){  //ID o keyWord                              
                        M.avanzar();    estado=20;
                        break;
                    }
                    
                    if (c == '"'){  //STRINGctte                            
                        M.avanzar();    estado=30;
                        break;
                    }

                    if ( (nom = Token.getNomToken(c)) != -1){   //Chars especiales y operadores.
                        M.avanzar();
                        R.setNom(nom);
                        return;
                    }   
                        //otro
                    R.setNom(Token.ERROR);
                    String m = "Error Léxico: char '"+c+"' (ASCII "+(int)c+"), no permitido aquí";
                    error.setError(m, pos, ac);
                    M.avanzar();
                    return;
                            
                case 10 :       //Reconocer NUM
                            while ( Character.isDigit(M.cc()) ){
                              ac += M.cc();
                              M.avanzar();
                            }
                            
                            R.set(Token.NUM, Integer.parseInt(ac));
                            return;
                            
                case 20:        //Reconocer ID o Keyword
                            while ( letra(M.cc()) || Character.isDigit(M.cc()) ){
                              ac += M.cc();
                              M.avanzar();
                            }
                            
                            Token t = getToken(ac);
                            if (t != null)
                                R.set(t);   //ac es una keyWord
                            else
                                R.set(Token.ID, -1);
                            
                            return;            
                            
                case 30:        //Reconocer STRINGctte
                           while (M.cc() != Cinta.EOF && M.cc()!=Cinta.EOLN){
                               c = M.cc();
                               ac += c;
                               M.avanzar();
                               if (c=='"'){
                                   R.set(Token.STRINGctte, 0);
                                   return;
                               }
                           }
                           
                           R.setNom(Token.ERROR);
                           error.setError("String excede la línea", pos, ac);
                           return;
                            
                case 100:   //Salir del dt.
                            return;                          
           }
       }
    } //End dt
    
    
    private boolean espacio(char cc){
        return (cc==Cinta.EOLN || cc==32 || cc==9);
    }
    
    private boolean letra(char cc){
        cc = Character.toUpperCase(cc);        
        return ('A'<= cc && cc <='Z');
    }
    
    private void avEspacios(Cinta M){   //"Comerse" los espacios iniciales.
        while (espacio(M.cc()))
            M.avanzar();
    }
    
    private int divOrComment(){            
        if (M.cc() != '/')          
           return -1;       //No es DIV ni comentario
        
        M.avanzar();
        
        if (M.cc() != '/'){
            ac = "/";
            R.setNom(Token.DIV);
            return 100;             //Ir al estado 100 (salir del dt)
        }
        
            //Quitar comentario de línea.
        while (M.cc()!=Cinta.EOF && M.cc() != Cinta.EOLN)
            M.avanzar();
        
        return 0;       //Volver al estado 0.
    }   
    
    private int paOrComment(){  
        if (M.cc() != '(')
            return -1;      //No es PA ni comentario de línea.
        
        M.avanzar();
        
        if (M.cc() != '*'){
            ac = "(";
            R.setNom(Token.PA);
            return 100;
        }    
        
            //Quitar comentario multilínea.
        M.avanzar();
        char ant = 0;
        
        while (M.cc()!=Cinta.EOF){
            char c = M.cc();
            M.avanzar();
            
            if (ant == '*' && c==')')
                return 0;       //Volver al estado 0.
                        
            ant = c;
        }    
        
        ac = "(*";
        R.setNom(Token.ERROR);
        
        error.setError("Este comentario no ha sido cerrado", pos, ac);
        return 100;
    }
    
    private int commentLlave(){
        if (M.cc() != '{')
            return -1;      //No es un comentario multilínea.
        
        M.avanzar();
                
        while (M.cc()!=Cinta.EOF){
            char c = M.cc();
            M.avanzar();
            
            if (c=='}')               
                return 0;       //Volver al estado 0.                          
        }    
        
        ac = "{";
        R.setNom(Token.ERROR);
        
        error.setError("Este comentario no ha sido cerrado", pos, ac);
        return 100;     //Ir al estado 100 (salir del dt)
    }    
    
    private int dosPuntosOrAssign(){
         if (M.cc() != ':')
            return -1;
         
         M.avanzar();
        
        if (M.cc() != '='){
            ac = ":";
            R.setNom(Token.DOSPTOS);    
        }
        else{
            M.avanzar();
            ac = ":=";
            R.setNom(Token.ASSIGN);
        }
        
        return 100;             //Ir al estado 100 (salir del dt)
    }
    
     private int oprelOrNot(){
        switch(M.cc()){
            case '=':   M.avanzar();
                        R.set(Token.OPREL, Token.IGUAL);
                        ac = "=";
                        return 100;     //Ir al estado 100 (salir del dt)
                        
            case '<':   M.avanzar();
                        if (M.cc() == '>'){
                            M.avanzar();
                            R.set(Token.OPREL, Token.DIS);
                            ac = "<>";
                            return 100;
                        }        
                        
                        if (M.cc() == '='){
                            M.avanzar();
                            R.set(Token.OPREL, Token.MEI);
                            ac = "<=";
                            return 100;
                        }
                        
                        R.set(Token.OPREL, Token.MEN);
                        ac = "<";
                        return 100;
                        
            case '>':   M.avanzar();
                        if (M.cc() == '='){
                            M.avanzar();
                            R.set(Token.OPREL, Token.MAI);
                            ac = ">=";
                            return 100;
                        }          
                        
                        R.set(Token.OPREL, Token.MAY);
                        ac = ">";
                        return 100;
                        
            case '!':   M.avanzar();
                        if (M.cc() == '='){
                            M.avanzar();
                            R.set(Token.OPREL, Token.DIS);
                            ac = "!=";
                            return 100;
                        }          
                        
                        R.setNom(Token.NOT);
                        ac = "!";
                        return 100;
        }
        
        return -1;      //No se pudo crear un OPREL o NOT, partiendo de M.cc()
    }
    
//----------------------------- TPC --------------------------------------------
    private Token t;
    
    private Token getToken(String keyword){
        if (t == null)
            t = new Token();
        
        keyword = keyword.toUpperCase();
        for (int i=0; i < KEYWORD.length; i++){
            if (keyword.equals(KEYWORD[i])){
                t.setNom(i);
                return t;
            }
        }
        
            //Verificar si keyword es un tipo
        int atrTipo = Token.getAtrTipo(keyword);
        
        if (atrTipo != 0){
            t.set(Token.TIPO, atrTipo);
            return t;
        }
            
        return null;    //El lexema keyword no es una Palabra Reservada
    }
    
    private static final String KEYWORD[] ={
        "","",
        "PROGRAM","VAR","PROCEDURE","BEGIN","END",
        "IF","THEN","ELSE","FOR","TO","DO","DOWNTO","WHILE","CASE","OF","REPEAT","UNTIL",
        "READ","READLN","WRITE","WRITELN",
        "","","","","","",
        "NOT","AND","OR","","","","MOD","DIV",
        "","","","",""
    };
    
}
