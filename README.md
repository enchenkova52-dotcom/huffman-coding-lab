Huffman Coding Lab



Программа реализует кодирование и декодирование файлов по методу Хаффмана.



Файлы в репозитории:



Huffman.java - исходный код программы.

FORMAT.txt - описание структуры закодированного файла.

same10.txt - тестовый файл из 10 одинаковых символов.

three20.txt - тестовый файл из 20 байт с тремя символами.

Huffman.class - скомпилированный class-файл.

\*.huf - закодированные файлы.

\*\_decoded.\* - файлы после декодирования.



Сборка:



javac Huffman.java



Кодирование:



java Huffman encode input.txt output.huf



Декодирование:



java Huffman decode input.huf restored.txt



Тест 1:



Set-Content -NoNewline -Encoding ascii same10.txt "1111111111"

java Huffman encode same10.txt same10.huf

java Huffman decode same10.huf same10\_decoded.txt

cmd /c fc same10.txt same10\_decoded.txt



Тест 2:



Set-Content -NoNewline -Encoding ascii three20.txt "11111111112222233333"

java Huffman encode three20.txt three20.huf

java Huffman decode three20.huf three20\_decoded.txt

cmd /c fc three20.txt three20\_decoded.txt



Тест 3:



javac Huffman.java

java Huffman encode Huffman.class HuffmanClass.huf

java Huffman decode HuffmanClass.huf HuffmanClass\_decoded.class

cmd /c fc /b Huffman.class HuffmanClass\_decoded.class

