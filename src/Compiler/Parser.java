package Compiler;

public class Parser {
    private Analex analex;
    private CError error;
    
    public Parser(Analex analex, CError error){
        this.analex = analex;
        this.error  = error;
    }
    
    public void programa(){ //Símbolo inicial.
      // header();
       //bloque();
//       impresion();
       //lectura();
       //asignacion();
       // programa -> header cuerpo main

//       antesDelMain();
       //main();
 //       contador();
 //       match(Token.TIPO, "BOOLEAN expected");
    header();
    cuerpo();
            match(Token.BEGIN, "se esperaba BEGIN");
            match(Token.END, "se esperaba END");
            match(Token.PTOCOMA, "; expected");    

  
    }
    private void antesDelMain(){
        int n = analex.preanalisis().getNom();
        if (n == Token.PROCEDURE || n == Token.VAR){
            cuerpo();
        }
        else if (n == Token.PROGRAM){
            header();
        }else{
            match(Token.BEGIN, "se esperaba BEGIN");
            match(Token.END, "se esperaba END");
            match(Token.PTOCOMA, "; expected");
        }
    }
    private void cuerpo(){
        // cuerpo -> procedimiento declaracion cuerpo | declaracion procedimiento cuerpo | lambda
        int n = analex.preanalisis().getNom();
        if (n == Token.PROCEDURE){
            procedimiento();
            declaracion();
            cuerpo();
        }
        else if (n == Token.VAR){
            declaracion();
            procedimiento();
            cuerpo();
        }else{
            //lambda
        }
        
    }
    private void main(){
        match(Token.BEGIN, "se esperaba BEGIN");
        //masSentencia();
        match(Token.END, "se esperaba END");
        match(Token.PTOCOMA, "; expected"); 
    }
    private void procedimiento(){
        // procedimiento -> PROCEDURE ID; bloque
        match(Token.PROCEDURE, "se esperaba PROCEDURE");
        match(Token.ID, "se esperaba un ID");
        match(Token.PTOCOMA, "; expected");
        bloque();
    }
    
    private void header(){ // header -> PROGRAM ID; | lambda
        /* F(header) = F(PROGRAM ID) | F(lambda)
                     = {PROGRAM, lambda}
        */
        if (analex.preanalisis().getNom() == Token.PROGRAM) {
            analex.avanzar();
            match(Token.ID, "Falta un identificador");
            match(Token.PTOCOMA, "; expected");
        }else{
            // lambda
        }  
    }
    
    private void declaracion(){
        // decl  -> var ID decl2 : tipo ; decl1
        // decl1 -> ID decl2 : tipo ; decl1 | lambda
        // decl2 -> , ID decl2 | lambda
        // tipo --> boolean | integer
        if (analex.preanalisis().getNom() == Token.VAR){
            match(Token.VAR, "se esperaba VAR");
            match(Token.ID, "se esperaba ID");
            decl2();
            match(Token.DOSPTOS, ": expected");
            tipo();
            match(Token.PTOCOMA, "; expected");
            decl1();
        }
    }
    private void decl1(){
         if (analex.preanalisis().getNom() == Token.ID){
             match(Token.ID, "se esperaba ID");
             decl2();
             match(Token.DOSPTOS, ": expected");
             tipo();
             match(Token.PTOCOMA, "; expected");
             decl1();
         }else{
             // lambda
         }
    }
    private void decl2(){
        if (analex.preanalisis().getNom() == Token.COMA){
            match(Token.COMA, ", expected");
            match(Token.ID, "se esperaba ID");
            decl2();
        }
        else {
            //lambda
        }
    }
    private void tipo(){
        if (analex.preanalisis().getNom() == Token.TIPO){
            match(Token.TIPO, "se esperaba un BOOLEAN o INTEGER");
       
        }else{
            Error("BOOLEAN or INTEGER expected");
        }
    }

    private void masSentencia(){ // masSentencia -> sentencia masSentencia | lambda
        int n = analex.preanalisis().getNom();
        if (n == Token.IF || n == Token.WHILE || n == Token.ID || n == Token.FOR || n == Token.REPEAT ||
            n == Token.CASE || n == Token.READ || n == Token.WRITE || n == Token.WRITELN){
            sentencia();
            masSentencia();
        }else{
            // lambda
        }
    }
    
    private void sentencia(){
        //  sentencia -> condicional | mientras | asignacion | contador | repetir | multiple | lectura | impresion
        //  F(sentencia) = {IF}      | {WHILE}  | {ID}       | {FOR}    | {REPEAT}| {CASE}   | {READ}  | {WRITE, WRITELN}
        //  F(sentencia) = {IF, WHILE, ID, FOR, REPEAT, CASE, READ, WRITE, WRITELN}
        int n = analex.preanalisis().getNom();
        if (n == Token.IF){
            condicional();
        }
        else if (n == Token.WHILE){
            mientras();
        }
        else if (n == Token.ID){
            asignacion();
        }
        else if (n == Token.FOR){
            contador();
        }
        else if (n == Token.REPEAT){
            repetir();
        }
        else if (n == Token.CASE){
            multiple();
        }
        else if (n == Token.READ){
            lectura();
        }
        else if (n == Token.WRITE || n == Token.WRITELN){
            impresion();
        }
    }
    
    private void condicional(){ // IF ..
        /*  condicional -> IF ExprBool THEN S1 S2
            S1        -> condicional   | bloque
            S2        -> ELSE bloque | lambda
        
            F(condicional) = F(IF ExprBool THEN S1 S2)
                         = {IF}
            F(S1)        = F(condicional)  | F(bloque)
                         = {IF}          | {BEGIN}
            F(S2)        = F(ELSE bloque)| F(lambda)
                         = {ELSE}        | {lambda}
        */
        match(Token.IF, "Se esperaba un IF");
        ExprBoole();
        match(Token.THEN, "Se esperaba un THEN");
        S1();
        S2();
    }
    private void S1(){
        if (analex.preanalisis().getNom() == Token.IF){
            condicional();
        }
        else if (analex.preanalisis().getNom() == Token.BEGIN){
            bloque();
        }
    }
    private void S2(){
        if (analex.preanalisis().getNom() == Token.ELSE){
            match(Token.ELSE, "se esperaba un ELSE");
            bloque();
        }else {
            // lambda
        }
    }
    
 //------------------------------------------------------------------------------------------------------------------
    
    private void mientras(){    // mientras -> WHILE ExprBool DO sentencia | WHILE ExprBool DO bloque
        /*
        mientras  -> WHILE ExprBool DO mientras2
        mientras2 -> sentencia | bloque
        F(mientras) = {WHILE}
        F(mientras2)= {IF, WHILE, ID, FOR, REPEAT, CASE, READ, WRITE, WRITELN} | {BEGIN}
        */
        match(Token.WHILE, "se esperaba WHILE");
        ExprBoole();
        match(Token.DO, "se esperaba DO");
        mientras2();
    }
    private void mientras2(){
        int n = analex.preanalisis().getNom();
        
        if (n == Token.IF || n == Token.WHILE || n == Token.ID || n == Token.FOR || n == Token.REPEAT ||
            n == Token.CASE || n == Token.READ || n == Token.WRITE || n == Token.WRITELN){
            sentencia();
        }
        else if(n == Token.BEGIN){
            bloque();
        }
    }
    
    // -------------------------------------------------------------------------
    // PROBADO
    private void bloque(){  // BEGIN masSentencia END;
        // F(bloque) = {BEGIN}
        match(Token.BEGIN, "Se esperaba BEGIN");
        masSentencia();
        match(Token.END, "Se esperaba END");  
        match(Token.PTOCOMA, "; expected");
    }
    
    // -------------------------------------------------------------------------
    // PROBADO
    private void asignacion(){  // ID := Expr;
        match(Token.ID, "se esperaba un ID");
        match(Token.ASSIGN, ":= expected");
        Expr();
        match(Token.PTOCOMA, "; expected");
        
    }
    
    // -------------------------------------------------------------------------
    // PROBADO
    private void contador(){
        // contador -> FOR asig cont1 Expr DO cont2
        // cont1    -> TO        | DOWNTO
        // cont2    -> sentencia | bloque
        //System.out.println("encuentra al for");
        match(Token.FOR, "se esperaba FOR");
        asig();
        cont1();
        Expr();
        match(Token.DO, "se esperaba DO");
        cont2();
        
    }
    private void asig(){
        match(Token.ID, "se esperaba un ID");
        match(Token.ASSIGN, ":= expected");
        Expr();
    }
    private void cont1(){
        if (analex.preanalisis().getNom() == Token.TO){
            match(Token.TO, "se esperaba TO");
        }
        else if (analex.preanalisis().getNom() == Token.DOWNTO){
            match(Token.DOWNTO, "se esperaba DOWNTO");
        }else {
            Error("se esperaba TO o DOWNTO");
        }
    }
    private void cont2(){
        int n = analex.preanalisis().getNom();
        
        if (n == Token.IF || n == Token.WHILE || n == Token.ID || n == Token.FOR || n == Token.REPEAT ||
            n == Token.CASE || n == Token.READ || n == Token.WRITE || n == Token.WRITELN){
            sentencia();
        }
        else if(n == Token.BEGIN){
            bloque();
        }
        else{
            Error("se esperaba una sentencia o un bloque");
        }
    }
    
    //--------------------------------------------------------------------------
    
    private void repetir(){
        /*
        repetir -> REPEAT masSentencia UNTIL ExprBoole;
        */
        match(Token.REPEAT, "se esperaba un REPEAT");
        masSentencia();
        match(Token.UNTIL, "se esperaba un UNTIL");
        ExprBoole();
        match(Token.PTOCOMA, "; expected");
    }
    
    //--------------------------------------------------------------------------
    
    private void multiple(){
        /*
        multiple -> CASE Expr OF mult1 END
        mult1    -> NUM: mult2 mult1 | lambda
        mult2    -> sentencia | bloque
        */
        match(Token.CASE, "se esperaba CASE");
        Expr();
        match(Token.OF, "se esperaba OF");
        mult1();
        match(Token.END, "se esperaba END");
    }
    private void mult1(){
        if (analex.preanalisis().getNom() == Token.NUM){
            match(Token.NUM, "se esperaba NUM");
            match(Token.DOSPTOS, ": expected");
            mult2();
            mult1();
        }else{
            // lambda
        }
    }
    private void mult2(){
        int n = analex.preanalisis().getNom();
        
        if (n == Token.IF || n == Token.WHILE || n == Token.ID || n == Token.FOR || n == Token.REPEAT ||
            n == Token.CASE || n == Token.READ || n == Token.WRITE || n == Token.WRITELN){
            sentencia();
        }
        else if(n == Token.BEGIN){
            bloque();
        }
    }
    
    //--------------------------------------------------------------------------
    // PROBADO
    
    private void lectura(){
        /*
        lectura -> READ(ID lect1);
        lect1   -> , ID lect1 | lambda
        */
        match(Token.READ, "se esperaba READ");
        match(Token.PA, "( expected");
        match(Token.ID, "se esperaba ID");
        lect1();
        match(Token.PC, ") expected");
        match(Token.PTOCOMA, "; expected");
    }
    private void lect1(){
        if(analex.preanalisis().getNom() == Token.COMA){
            match(Token.COMA, ", expected");
            match(Token.ID, "se esperaba ID");
            lect1();
        }
        else {
            // lambda
        }
    }
    
    // ---------------------------------------------------------------
    // PROBADO
    
    private void impresion(){
        /*
        impresion -> impr1 (arg1 impr2);
        impr1     -> WRITE | WRITELN
        arg1      -> Expr  | STRINGCTTE
        impr2     -> , arg1 impr2 | lambda
        */
        impr1();
        match(Token.PA, "( expected");
        arg1();
        impr2();
        match(Token.PC, ") expected");
        match(Token.PTOCOMA, "; expected");
    }
    private void impr1(){
        if (analex.preanalisis().getNom() == Token.WRITE){
            match(Token.WRITE, "se esperaba WRITE");
        }
        else if (analex.preanalisis().getNom() == Token.WRITELN){
            match(Token.WRITELN, "se esperaba WRITELN");
        }
    }
    private void arg1(){
        if (analex.preanalisis().getNom() == Token.NUM || analex.preanalisis().getNom() == Token.ID){
            Expr();
        }
        else if(analex.preanalisis().getNom() == Token.STRINGctte){
            match(Token.STRINGctte, "se esperaba un STRINGctte");
        }else {
            Error("se experaba Expr o STRINGctte");
        }
    }
    private void impr2(){
        if (analex.preanalisis().getNom() == Token.COMA){
            match(Token.COMA, ", expected");
            arg1();
            impr2();
        }else {
            // lambda
        }
    }
    
    //--------------------------------------------------------------------------
    

    
//---------    
    private void match(int nomtoken, String msjError){
        if (analex.preanalisis().getNom() == nomtoken)
            analex.avanzar();
        else
           Error(msjError); 
    }
    
    private void Error(String msjError){
       error.setError(msjError, analex.getPos(), analex.lexema()); 
    }
    
//------------------- Expr y ExprBoole -----------------------------------------
    private void Expr(){    //Expr -> termino Expr1
        if (error.hayError()) return;
        
        if (inFirstExpr( analex.preanalisis().getNom() )){
            termino();
                //Expr1 -> + Termino Expr1 | - Termino Expr1 | lambda 
            int n = analex.preanalisis().getNom();
            while (n==Token.MAS || n==Token.MENOS){
                analex.avanzar();
                termino();
                n = analex.preanalisis().getNom();
            }    
        }
        else
            Error("Se espera una Expr");
    }
    
    
    private void termino(){ //termino->factor termino1
        if (error.hayError()) return;
        
        factor();
        
            //termino1 -> * factor Termino1 | / factor termino1 | % factor termino1
        int n = analex.preanalisis().getNom();
        while (n==Token.POR || n==Token.DIV || n==Token.MOD){
                analex.avanzar();
                factor();
                n = analex.preanalisis().getNom();
        }
        
    }
    
    private void factor(){
        if (error.hayError()) return;
        
        int n = analex.preanalisis().getNom();
        if (n==Token.MAS || n==Token.MENOS){
            analex.avanzar();   //match + o -
            factor();
        }
        else
           if (n==Token.ID || n==Token.NUM) 
               analex.avanzar();    //match ID o NUM
           else
             if (n==Token.PA){
                 analex.avanzar();  //match (
                 Expr();
                 match(Token.PC, "Se espera )");
             }
             else
                 Error("Se espera un operando");
    }
    
    
    private void ExprBoole(){ //ExprBoole -> Literal op ExprBoole | Literal  
        literal();
        int n = analex.preanalisis().getNom();
        while (n==Token.AND || n==Token.OR){
                analex.avanzar();
                literal();
                n = analex.preanalisis().getNom();
        }
    }
    
    private void literal(){ //Literal -> (Expr OPREL Expr) | NOT Literal
        int n = analex.preanalisis().getNom();
        if (n == Token.PA){
            analex.avanzar();
            literalPuro();
            match(Token.PC, "Se espera )");
        }
        else
            if (inFirstExpr(n))
                 literalPuro();
            else
                if (n==Token.NOT){
                    analex.avanzar();
                    literal();
                }    
                else
                    Error("Error en factor boole");          
    }
    
    private void literalPuro(){
        Expr();
        match(Token.OPREL, "Se espera un operador relacional");
        Expr();
    }
    
    private boolean inFirstExpr(int nom){
      return (nom==Token.ID || nom==Token.NUM || nom==Token.MAS ||
              nom==Token.MENOS || nom==Token.PA);  
    }
}
