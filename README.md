<h3>Java Translator</h3>
<p>
    <b> Main point of the project is to create a java translator from a "handmade" language. </b>
</p>
<h4>What is translator?</h4>
<p> 
    A translator or programming language processor is a computer program that performs the translation of a program written in a given programming language into a functionally equivalent program in another computer language (the target language), without losing the functional or logical structure of the original code (the "essence" of each program).
    <br><a href="https://en.wikipedia.org/wiki/Translator_(computing)">Wiki page</a>
</p>
<h4>How to use it?</h4>
<p>
    <ul>
        <li>
            <strong>Add a dependency to your project</strong>
            <ul>
                <li>
                    <p>Maven</p>
                    <pre><code>&lt;dependency>
    &lt;groupId>io.github.therealmone&lt;/groupId>
    &lt;artifactId>JavaTranslator&lt;/artifactId>
    &lt;version>1.1.0&lt;/version>
    &lt;classifier>jar-with-dependencies&lt;/classifier>
&lt;/dependency></code></pre>
                </li>
                <li>
                    <p>Gradle
                    <pre><code>compile 'io.github.therealmone:JavaTranslator:1.1.0:jar-with-dependencies'</code></pre>
               </li>
               <li>
                <p>Apache Ivy
                <pre><code>&lt;dependency org="io.github.therealmone" name="JavaTranslator" rev="1.1.0" />
    &lt;artifact name="JavaTranslator" type="jar" ext="jar" m:classifier="jar-with-dependencies" /></code></pre>
               </li>
            </ul>
        </li>
        <li>
            <strong>Now you can create Translator object and invoke translate() method</strong>
            <pre><code>Translator translator = Translator.create();
translator.translate("new a = 100; print(a);");</code></pre>
        </li>
        <li>
            <strong>You can also load program from InputStream</strong>
            <pre><code>translator.translate(Translator.loadProgram(new FileInputStream("example.txt")));</code></pre>
        </li>
    </ul>
</p>