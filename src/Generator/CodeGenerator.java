package Generator;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CodeGenerator {
    private static MethodSpec.Builder main = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class)
            .addParameter(String[].class, "args");

    private static TypeSpec.Builder compiled = TypeSpec.classBuilder("First")
            .addModifiers(Modifier.PUBLIC)
            .addMethod(main.build());

    public static MethodSpec.Builder addRoutine(String name, String parameters, ArrayList<String> statements, String returnType) {
            MethodSpec.Builder routine = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(new TypeName(returnType))

    }

    public static FieldSpec.Builder addArray(MethodSpec.Builder builder) {

    }

    public static TypeSpec.Builder addRecord() {

    }

    public static void addStatement(MethodSpec.Builder builder, String statement){
    }

    public static void compile() throws IOException {
        JavaFile javaFile = JavaFile.builder("", compiled.build()).build();
        javaFile.writeTo(new File(""));
    }

}
