package frc.robot;

import static frc.robot.Enums.DashButton;
import static frc.robot.Enums.DashValue;
import static frc.robot.Enums.RobotValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;

import static frc.robot.Enums.RobotStatus;
import static frc.robot.Enums.RobotMode;

public class Dashboard
{
    private static final int BUFFER_LEN = 1024;
    private static final int TCP_PORT   = 5801;

    private class button
    {
        public int state;
        public int pressed;
    }

    private String _commandPrefix;
    private int _dashboardButtonCount;
    private int _dashboardValueCount;
    private int _robotStatusCount;
    private int _robotValueCount;
    private int _robotMode;

    private int[] _robotStatus;
    private double[] _robotValue;
    private button[] _dashboardButton;
    private double[] _dashboardValue;

    public Dashboard(String commandPrefix, int robotStatusCount, int robotValueCount, int dashButtonCount, int dashValueCount)
    {
        writeToLog("INIT");

        _robotMode = 0;

        _commandPrefix = commandPrefix;
        _robotStatusCount = robotStatusCount;
        _robotValueCount = robotValueCount;
        _dashboardButtonCount = dashButtonCount;
        _dashboardValueCount = dashValueCount;

        _robotStatus = new int[_robotStatusCount];
        _robotValue = new double[_robotValueCount];
        _dashboardButton = new button[_dashboardButtonCount];
        _dashboardValue = new double[_dashboardValueCount];

        for (int i = 0; i < _robotStatusCount; i++) _robotStatus[i] = 0;
        for (int i = 0; i < _robotValueCount; i++) _robotValue[i] = 0;
        for (int i = 0; i < _dashboardButtonCount; i++)
        {
            _dashboardButton[i] = new button();
            _dashboardButton[i].state = 0;
            _dashboardButton[i].pressed = 0;
        }

        // Read Settings File
    }

    public String countReply()
    {
        return "COUNT:" + dataString(_robotMode, 1) + dataString(_robotStatusCount, 1) + dataString(_robotValueCount, 1) + dataString(_dashboardButtonCount, 1) + dataString(_dashboardValueCount, 0) + "\r\n";
    }

    public String dataString(int number, int delimiter)
    {
        switch (delimiter)
        {
            case 1:  return String.valueOf(number) + ",";
            case 2:  return String.valueOf(number) + "|";
            default: return String.valueOf(number);
        }
    }

    public String dataString(double number, int delimiter)
    {
        var nf = new DecimalFormat("##.###");

        switch (delimiter)
        {
            case 1:  return nf.format(number) + ",";
            case 2:  return nf.format(number) + "|";
            default: return nf.format(number);
        }
    }

    public String getCommandPrefix()
    {
        return _commandPrefix;
    }

    public boolean getDashButton(DashButton buttonIndex)
    {
        int group = buttonIndex.ordinal() / 16;
        int index = buttonIndex.ordinal() % 16;

        if (index < 16 && group < _dashboardButtonCount) 
        {
            return ((_dashboardButton[group].state & (1 << index)) != 0);
        }
        
        return false;
    }

    public boolean getDashButtonPress(DashButton buttonIndex)
    {
        int group = buttonIndex.ordinal() / 16;
        int index = buttonIndex.ordinal() % 16;
        boolean vReturn = false;

        if (group < _dashboardButtonCount)
        {
            int buttonValue = 1 << index;

            if ((_dashboardButton[group].state & buttonValue) != 0)
            {
                vReturn = ((_dashboardButton[group].pressed & buttonValue) == 0);
                _dashboardButton[group].pressed |= buttonValue;
            }
            else if ((_dashboardButton[group].pressed & buttonValue) != 0)
            {
                _dashboardButton[group].pressed ^= buttonValue;
            }
        }

        return vReturn;
    }

    public double getDashValue(DashValue valueIndex)
    {
        if (valueIndex.ordinal() < _dashboardValueCount) return _dashboardValue[valueIndex.ordinal()];
        return 0;
    }

    public String getReply()
    {
        String data = "GET:" + dataString(_robotMode, 2);

        if (_robotStatusCount > 0)
        {
            for (int i = 0; i < _robotStatusCount; i++)
            {
                data += dataString(_robotStatus[i], 1);
            }

            data = data.substring(0, data.length() - 1);
        }

        data += "|";

        if (_robotValueCount > 0)
        {
            for (int i = 0; i < _robotValueCount; i++)
            {
                data += dataString(_robotValue[i], 1);
            }

            data = data.substring(0, data.length() - 1);
        }

        data += "\r\n";

        return data;
    }

    public boolean getRobotStatus(RobotStatus statusIndex)
    {
        int group = statusIndex.ordinal() / 16;
        int index = statusIndex.ordinal() % 16;

        if (index < 16 && group < _robotStatusCount)
        {
            return ((_robotStatus[group] & (1 << index)) != 0);
        }

        return false;
    }

    public String pullReply()
    {
        String data = "PULL:" + dataString((double)_dashboardValueCount, 2);

        if (_dashboardValueCount > 0)
        {
            for (int i = 0; i < _dashboardValueCount; i++)
            {
                data += dataString(_dashboardValue[i], 1);
            }

            data = data.substring(0, data.length() - 1);
        }

        data += "\r\n";

        return data;
    }

    public void saveDashValues()
    {
        // Write dash file
    }

    public boolean setDashButton(int group, int value)
    {
        if (group < 0 || group >= _dashboardButtonCount)
        {
            return false;
        }

        _dashboardButton[group].state = value;

        return true;
    }

    public boolean setDashValue(int valueIndex, double value)
    {
        if (valueIndex < 0 || valueIndex >= _dashboardValueCount)
        {
            return false;
        }

        _dashboardValue[valueIndex] = value;

        return true;
    }

    public boolean setRobotStatus(RobotStatus statusIndex, boolean value)
    {
        int group = statusIndex.ordinal() / 16;
        int index = statusIndex.ordinal() % 16;

        if (group > _robotStatusCount)
        {
            return false;
        }

        int statusValue = 1 << index;

        if (value)
        {
            _robotStatus[group] |= statusValue;
        }
        else if ((_robotStatus[group] & statusValue) != 0)
        {
            _robotStatus[group] ^= statusValue;
        }

        return true;
    }

    public boolean setRobotValue(RobotValue valueIndex, double value)
    {
        if (valueIndex.ordinal() < 0 || valueIndex.ordinal() >= _robotValueCount)
        {
            return false;
        }

        _robotValue[valueIndex.ordinal()] = value;

        return true;
    }

    public void setRobotMode(RobotMode mode)
    {
        _robotMode = mode.ordinal();
    }

    public void startHost()
    {
        writeToLog("Start Host Thread");

        Dashboard host = this;

        Thread thread = new Thread(
            new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        tcpLoop(host);
                    }
                    catch(Exception e)
                    {
                        writeToLog("Error starting dashboard: " + e.getMessage());
                    }
                }
            }
        );
        thread.start();
    }

    public void writeToLog(String entry)
    {
        // Write to log
    }

    public static void tcpLoop(Dashboard host) throws IOException
    {
        String commandEnd = "\r\n";
        String commandCount = host.getCommandPrefix() + "COUNT";
        String commandGet = host.getCommandPrefix() + "GET";
        String commandPull = host.getCommandPrefix() + "PULL";
        String commandPut = host.getCommandPrefix() + "PUT";
        String commandSet = host.getCommandPrefix() + "SET";

        String command;
        String reply;
        String clientMsg;
        String recMsg = "";

        int    position;
        int    index;
        int    replySize;

        @SuppressWarnings("resource")
        ServerSocket hostSocket = new ServerSocket(TCP_PORT);
        Socket       clientSocket;

        hostSocket.setSoTimeout(10000);
        hostSocket.setReuseAddress(true);

        char[] recBuffer = new char[BUFFER_LEN];
        byte[] sendBuffer = new byte[BUFFER_LEN];

        while (true)
        {
            try
            {
                clientSocket = hostSocket.accept();
            }
            catch (IOException e)
            {
                host.writeToLog("Client Accept Timeout");
                continue;
            }

            if (clientSocket != null)
            {
                host.writeToLog("Connection Accepted");

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (in.read(recBuffer, 0, BUFFER_LEN) > 0)
                {
                    String tmp = new String(recBuffer);

                    if ((position = tmp.lastIndexOf(commandEnd)) >= 0)
                    {
                        tmp = tmp.substring(0, position + 2);
                    }

                    recMsg += tmp;
                    recBuffer = new char[BUFFER_LEN];

                    while ((position = recMsg.indexOf(commandEnd)) >= 0)
                    {
                        clientMsg = recMsg.substring(0, position);
                        recMsg = recMsg.substring(position + 2);

                        if ((position = clientMsg.indexOf(":")) >= 0)
                        {
                            command = clientMsg.substring(0, position);
                            clientMsg = clientMsg.substring(position + 1);
                            reply = "";

                            if (commandCount.equals(command))
                            {
                                reply = host.countReply();
                            }
                            else if (commandGet.equals(command))
                            {
                                reply = host.getReply();
                            }
                            else if (commandPull.equals(command))
                            {
                                reply = host.pullReply();
                            }
                            else if (commandPut.equals(command))
                            {
                                reply = "PUT:";
                                boolean saveFile = false;

                                while ((position = clientMsg.indexOf("|")) >= 0)
                                {
                                    command = clientMsg.substring(0, position);
                                    clientMsg = clientMsg.substring(position + 1);

                                    if ((position = command.indexOf(",")) >= 0)
                                    {
                                        String group = command.substring(0, position);
                                        command = command.substring(position + 1);

                                        if ((position = command.indexOf(",")) >= 0)
                                        {
                                            index = Integer.parseInt(command.substring(0, position));

                                            if ("V".equals(group))
                                            {
                                                if (host.setDashValue(index, Double.parseDouble(command.substring(position + 1))))
                                                {
                                                    reply += "V," + host.dataString(index, 2);
                                                    saveFile = true;
                                                }
                                            }
                                            else if ("B".equals(group))
                                            {
                                                if (host.setDashButton(index, Integer.parseInt(command.substring(position + 1))))
                                                {
                                                    reply += "B," + host.dataString(index, 2);   
                                                }
                                            }
                                        }
                                    }
                                }

                                reply += "\r\n";

                                if (saveFile)
                                {
                                    host.saveDashValues();
                                }
                            }
                            else if (commandSet.equals(command))
                            {
                                host.writeToLog("New Robot Setting(s) from Dashboard");
                            }

                            replySize = reply.length();

                            if (replySize > 0)
                            {
                                sendBuffer = reply.getBytes();

                                try
                                {
                                    clientSocket.getOutputStream().write(sendBuffer, 0, replySize);
                                }
                                catch (IOException e)
                                {
                                    host.writeToLog("TCP Send Error");
                                }
                            }
                        }
                    }
                }

                clientSocket.close();
                recBuffer = new char[BUFFER_LEN];
                recMsg = "";

                host.writeToLog("Connection Lost");
            }
        }
    }
}
