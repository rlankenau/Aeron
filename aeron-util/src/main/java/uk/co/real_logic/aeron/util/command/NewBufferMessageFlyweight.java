/*
 * Copyright 2014 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.real_logic.aeron.util.command;

import java.nio.ByteOrder;

/**
 * Message to denote that new buffers have been added for a subscription.
 *
 * @see uk.co.real_logic.aeron.util.command.ControlProtocolEvents
 *
 *  * 0                   1                   2                   3
 * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                          Session ID                           |
 * +---------------------------------------------------------------+
 * |                          Channel ID                           |
 * +---------------------------------------------------------------+
 * |                           Term ID                             |
 * +---------------------------------------------------------------+
 * |      Destination Length       |   Destination               ...
 * |                                                             ...
 * +---------------------------------------------------------------+
 * |      File Length              |   File                      ...
 * |                                                             ...
 * +---------------------------------------------------------------+
 */
public class NewBufferMessageFlyweight extends QualifiedMessageFlyweight
{

    private int lengthOfFile;

    /**
     * {@inheritDoc}
     */
    public NewBufferMessageFlyweight destination(final String destination)
    {
        super.destination(destination);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public NewBufferMessageFlyweight termId(final long termId)
    {
        super.termId(termId);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public NewBufferMessageFlyweight channelId(final long channelId)
    {
        super.channelId(channelId);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public NewBufferMessageFlyweight sessionId(final long sessionId)
    {
        super.sessionId(sessionId);
        return this;
    }

    private int endOfDestination()
    {
        return offset() + super.length();
    }

    /**
     * return file where the buffer is located.
     *
     * NB: you must read this after the destination field.
     *
     * @return file where the buffer is located
     */
    public String file()
    {
        return stringGet(endOfDestination(), ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * set file where the buffer is located
     *
     * NB: you must write this after the destination field.
     *
     * @param file the file where the buffer is located
     * @return flyweight
     */
    public NewBufferMessageFlyweight file(final String file)
    {
        lengthOfFile = stringPut(endOfDestination(),
                                 file,
                                 ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    /**
     * Get the length of the current message
     *
     * NB: must be called after the data is written in order to be accurate.
     *
     * @return the length of the current message
     */
    public int length()
    {
        return super.length();
    }
}