package index;

import lib.CharHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Index {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private ByteBuffer readBuffer;
    private SocketChannel socketChannel;

    public static void main(String[] args){
        Index i = new Index();
        i.init();
        i.startListen();
    }

    public void init(){
        try {
            readBuffer = ByteBuffer.allocate(2048);
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(9900));
            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListen(){
        while (true) {
            try {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if(key.isAcceptable()) {
                        socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);
                    } else if(key.isReadable()) {
                        socketChannel = (SocketChannel) key.channel();
                        readBuffer.clear();
                        int length = socketChannel.read(readBuffer);
                        if(length>0) {
                            readBuffer.flip();
                            CharBuffer charBuffer = CharHelper.decode(readBuffer);
                            if(charBuffer!=null) {
                                char[] chars = charBuffer.array();
                                String line = new String(chars);
                                System.out.println(line);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                try {
                    socketChannel.close();
                    serverSocketChannel.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

}
