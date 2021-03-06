package com.annotations.complier.lib;

import com.google.auto.service.AutoService;
import com.annotations.lib.*;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * 注解处理器，编译时，编译器会自动查找所有继承AbstractProcessor的类，触发process方法
 */
@SupportedAnnotationTypes({"com.annotations.lib.BindViews"})//注解类路径
@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {
    private static HashMap<String, String> hashMap;

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("2日志打印开始:");

        StringBuilder builder = new StringBuilder()
                .append("package com.annotations.generated;\n\n")
                .append("public class BindViews_Bind {\n\n") // open class
                .append("\tpublic String getMessage() {\n") // open method
                .append("\t\treturn \"");


        // for each javax.lang.model.element.Element annotated with the CustomAnnotation
        for (Element element : roundEnvironment.getElementsAnnotatedWith(MyBindView.class)) {
            String objectType = element.getSimpleName().toString();
            Object oBject = element.asType();
            // this is appending to the return statement
            builder.append(objectType).append(" this BindViews注解～");
        }

        builder.append("\";\n") // end return
                .append("\t}\n") // close method
                .append("}\n"); // close class


        try { // write the file
            JavaFileObject source = processingEnv.getFiler().createSourceFile("com.annotations.generated.BindViews_Bind");


            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }

        System.out.println("2日志打印结束:");
        return false;
    }
}
