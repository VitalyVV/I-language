package Generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

public class CodeGenerator {
    private static MethodSpec main = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class)
            .addParameter(String[].class, "args")
            .addStatement("$T.out.println($S)", System.class, "Edik pedik")
            .build();

    private static TypeSpec first = TypeSpec.classBuilder("First")
            .addModifiers(Modifier.PUBLIC)
            .addMethod(main)
            .build();

    private static JavaFile javaFile = JavaFile.builder("", first).build();

    public static void main(String[] args) throws IOException {
        javaFile.writeTo(new File(""));
    }
}
