package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Created by buthodgt on 1/13/17.
 */
public class DeclMethod extends AbstractDeclMethod {
    final private AbstractIdentifier type;
    final private AbstractIdentifier methodName;
    private ListDeclParam params;
    private AbstractBody body;
    private EnvironmentExp env_exp_body;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, ListDeclParam params, AbstractBody body) {
        Validate.notNull(type);
        Validate.notNull(methodName);
        Validate.notNull(body);
        Validate.notNull(params);
        this.type = type;
        this.methodName = methodName;
        this.params = params;
        this.body = body;
    }

    public AbstractIdentifier getMethodName(){
        return this.methodName;
    }

    public AbstractIdentifier getType(){
        return this.type;
    }

    public AbstractBody getBody() {
        return this.body;
    }


    @Override
    protected void verifyDeclMethodHeader(DecacCompiler compiler, ClassDefinition classDef)
            throws ContextualError {
        env_exp_body = new EnvironmentExp(classDef.getMembers());
        Signature sign = params.verifyListParam(compiler, env_exp_body, classDef);
        Type returnType = type.verifyType(compiler);
        //On déclare la méthode dans env_exp
        //A modifier s'il faut prendre en compte les fonctions de même nom mais signature différente.
        MethodDefinition def = new MethodDefinition(returnType, this.getLocation(), sign, classDef.incNumberOfMethods());
        try {
            classDef.getMembers().declare(methodName.getName(), def);
        }
        catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Une méthode de même nom a été déclarée 2 fois dans la classe.", this.getLocation());
        }
        methodName.setDefinition(def);
        methodName.setType(returnType);
    }

    @Override
    protected void verifyDeclMethodBody(DecacCompiler compiler, ClassDefinition classDef)
            throws ContextualError {
        body.verifyBody(compiler, env_exp_body, classDef, type.getType());
    }


    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        methodName.decompile(s);
        body.decompile(s);
        s.println("");
        throw new UnsupportedOperationException("Pas encore implémenté");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        methodName.iter(f);
        //Afficher la signature
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        //Afficher la signature de la méthode
        body.prettyPrint(s, prefix, false);
    }
}
