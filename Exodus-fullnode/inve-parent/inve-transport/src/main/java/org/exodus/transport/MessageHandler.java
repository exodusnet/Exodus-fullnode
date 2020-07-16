package org.exodus.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import rx.subjects.Subject;

/**
 * Channel handler for getting message traffic. Activated when connection established/accepted.
 * <p/>
 * <b>NOTE:</b> in the pipeline this handler must be set just right before {@link ExceptionHandler}.
 */
@ChannelHandler.Sharable
final class MessageHandler extends ChannelInboundHandlerAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

  private final Subject<Message, Message> incomingMessagesSubject;

  MessageHandler(Subject<Message, Message> incomingMessagesSubject) {
    this.incomingMessagesSubject = incomingMessagesSubject;
  }

  /**
   * Publish {@code msg} on the incoming messages observable.
   */
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    Message message = (Message) msg;
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Received: {}", message);
    }
    incomingMessagesSubject.onNext(message);
    //leaks memory 2018-08-15
    ReferenceCountUtil.release(msg);
  }
}
