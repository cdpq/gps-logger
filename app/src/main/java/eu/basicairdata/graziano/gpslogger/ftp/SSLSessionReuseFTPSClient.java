package eu.basicairdata.graziano.gpslogger.ftp;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Locale;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocket;

import org.apache.commons.net.ftp.FTPSClient;

/** FTPSClient class that supports session reuse.
 *
 * Workaround for FTPSClient from org.apache.commons.net.ftp.FTPSClient to support session reuse. Uses
 * some sort of reflection hack to make this possible. That said, this is not a permanent solution
 * since the rules could change and make the hack not possible anymore.
 *
 * @see org.apache.commons.net.ftp.FTPSClient
 * @see "https://stackoverflow.com/questions/32398754/how-to-connect-to-ftps-server-with-data-connection-using-same-tls-session"
 */
class SSLSessionReuseFTPSClient extends FTPSClient {

    /** Constructor for SSLSessionReuseFTPSClient. */
    public SSLSessionReuseFTPSClient() { super("TLS", true); }

    /** Constructor for SSLSessionReuseFTPSClient. Protocol is TLS by default.
     *
     * @param isImplicit The security mode (Implicit/Explicit)
     */
    public SSLSessionReuseFTPSClient(boolean isImplicit) {
        super("TLS", isImplicit);
    }

    /** Constructor for SSLSessionReuseFTPSClient allowing specification of protocol and security
     * mode.
     *
     * @param protocol The protocol ("TLS"/"SSL")
     * @param isImplicit The security mode (Implicit/Explicit)
     */
    public SSLSessionReuseFTPSClient(String protocol, boolean isImplicit) {
        super(protocol, isImplicit);
    }

    // Adapted from: https://trac.cyberduck.io/browser/trunk/ftp/src/main/java/ch/cyberduck/core/ftp/FTPClient.java
    @Override
    protected void _prepareDataSocket_(final Socket socket) throws IOException {
        if (socket instanceof SSLSocket) {
            final SSLSession session = ((SSLSocket) _socket_).getSession();

            if (session.isValid()) {
                final SSLSessionContext context = session.getSessionContext();

                try {
                    final Field sessionHostPortCache = context.getClass().getDeclaredField("sessionHostPortCache");
                    sessionHostPortCache.setAccessible(true);

                    final Object cache = sessionHostPortCache.get(context);
                    final Method method = cache.getClass().getDeclaredMethod("put", Object.class, Object.class);
                    method.setAccessible(true);

                    method.invoke(cache, String
                            .format("%s:%s", socket.getInetAddress().getHostName(), String.valueOf(socket.getPort()))
                            .toLowerCase(Locale.ROOT), session);

                    method.invoke(cache, String
                            .format("%s:%s", socket.getInetAddress().getHostAddress(), String.valueOf(socket.getPort()))
                            .toLowerCase(Locale.ROOT), session);
                } catch (NoSuchFieldException e) {
                    throw new IOException(e);
                } catch (Exception e) {
                    throw new IOException(e);
                }
            } else {
                throw new IOException("Invalid SSL Session");
            }
        }
    }
}
