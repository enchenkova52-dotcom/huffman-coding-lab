import java.io.*;
import java.util.*;

public class Huffman {

    static class Node implements Comparable<Node> {
        byte b;
        int freq;
        Node left, right;

        Node(byte b, int freq) {
            this.b = b;
            this.freq = freq;
        }

        Node(Node l, Node r) {
            this.b = 0;
            this.freq = l.freq + r.freq;
            this.left = l;
            this.right = r;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public int compareTo(Node o) {
            return this.freq - o.freq;
        }
    }

    // ===== build tree =====
    static Node buildTree(Map<Byte, Integer> freq) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (var e : freq.entrySet()) {
            pq.add(new Node(e.getKey(), e.getValue()));
        }

        while (pq.size() > 1) {
            Node a = pq.poll();
            Node b = pq.poll();
            pq.add(new Node(a, b));
        }

        return pq.poll();
    }

    // ===== build codes =====
    static void buildCodes(Node n, String s, Map<Byte, String> map) {
        if (n.isLeaf()) {
            map.put(n.b, s.length() > 0 ? s : "0");
            return;
        }
        buildCodes(n.left, s + "0", map);
        buildCodes(n.right, s + "1", map);
    }

    // ===== encode =====
    static void encode(String in, String out) throws Exception {
        byte[] data = readFile(in);

        Map<Byte, Integer> freq = new HashMap<>();
        for (byte b : data)
            freq.put(b, freq.getOrDefault(b, 0) + 1);

        Node root = buildTree(freq);

        Map<Byte, String> codes = new HashMap<>();
        buildCodes(root, "", codes);

        BitSet bits = new BitSet();
        int bitLen = 0;

        for (byte b : data) {
            String code = codes.get(b);
            for (char c : code.toCharArray()) {
                if (c == '1')
                    bits.set(bitLen);
                bitLen++;
            }
        }

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(out))) {

            dos.writeBytes("HUF1");
            dos.writeInt(codes.size());

            for (var e : codes.entrySet()) {
                dos.writeByte(e.getKey());
                dos.writeByte(e.getValue().length());
                dos.writeBytes(e.getValue());
            }

            dos.writeInt(bitLen);
            byte[] arr = bits.toByteArray();
            dos.write(arr);
        }
    }

    // ===== decode =====
    static void decode(String in, String out) throws Exception {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(in))) {

            byte[] magic = new byte[4];
            dis.readFully(magic);

            int n = dis.readInt();

            Map<String, Byte> reverse = new HashMap<>();

            for (int i = 0; i < n; i++) {
                byte symbol = dis.readByte();
                int len = dis.readByte();
                byte[] code = new byte[len];
                dis.readFully(code);
                reverse.put(new String(code), symbol);
            }

            int bitLen = dis.readInt();
            byte[] data = dis.readAllBytes();

            StringBuilder bits = new StringBuilder();

            for (byte b : data) {
                for (int i = 7; i >= 0; i--) {
                    bits.append(((b >> i) & 1));
                }
            }

            StringBuilder current = new StringBuilder();
            List<Byte> output = new ArrayList<>();

            for (int i = 0; i < bitLen; i++) {
                current.append(bits.charAt(i));
                if (reverse.containsKey(current.toString())) {
                    output.add(reverse.get(current.toString()));
                    current.setLength(0);
                }
            }

            try (FileOutputStream fos = new FileOutputStream(out)) {
                for (byte b : output)
                    fos.write(b);
            }
        }
    }

    static byte[] readFile(String path) throws Exception {
        return new FileInputStream(path).readAllBytes();
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: encode|decode input output");
            return;
        }

        if (args[0].equals("encode")) {
            encode(args[1], args[2]);
        } else if (args[0].equals("decode")) {
            decode(args[1], args[2]);
        }
    }
}