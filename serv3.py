
import socket
import sys

HOST ='192.168.0.102' #'192.168.0.100'#'192.168.1.102'
PORT = 8280

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print ('socket created')

#Bind socket to Host and Port
try:
    s.bind((HOST, PORT))
except socket.error as err:
    print ('Bind Failed, Error Code: ' + str(err[0]) + ', Message: ' + err[1])
    sys.exit()

print ('Socket Bind Success!')


#listen(): This method sets up and start TCP listener.
s.listen(10)
print ('Socket is now listening')


while 1:
    conn, addr = s.accept()
    print ('Connect with ' + addr[0] + ':' + str(addr[1]) ) #str(

    datta = "Client sSet data" + '\n'
    Host1 =  '' #'192.168.0.102'
    Port1 =  2 # 8280

    Host1 =  addr[0]
    Port1 =  addr[1]

    # sor = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)

    # sor.bind(('', 0)) # (('192.168.0.102', 8280))   # Задаем сокет как клиент
    # # # server = (Host1 , Port1)
    # sor.sendto(datta.encode('utf-8'), ((Host1 , Port1)) ) #server)# Уведомляем сервер о подключении

    print('Send data CLIENT')
    conn.sendall(datta.encode('utf-8'))
    print('2Send data CLIENT2')
    buf = conn.recv(64)
    
    print (buf)
    # addr.send(datta.encode('utf-8'))
    print ('data1')
    # conn.send(datta)
    print ('data2')
    conn.sendall(datta.encode('utf-8'))

s.close()
     # sor.connect(conn)
    # sor.send(datta)

    # sor.bind(('', 0)) # (('192.168.0.102', 8280))   # Задаем сокет как клиент
    # server = Host1 , Port1
    # sor.sendto(datta.encode('utf-8'), server)# Уведомляем сервер о подключении
#    sor.send(datta,1)
   # s.sendto( datta.encode('utf-8'), ( Host1 ,Port1 )  )  # message.decode()  message.encode()  # datta.encode('utf-8')  # 63595  # 192.168.0.102 : 63642

# conn ,addr=s.accept()
#  data=conn.recv(2000)
#  data.decode()


    # data , addres = sock.recvfrom(1024)
    #      print (addres[0], addres[1])
    #      if  addres not in client : 
    #              client.append(addres)# Если такого клиента нету , то добавить
    #      for clients in client :
    #              if clients == addres : 
    #                  continue # Не отправлять данные клиенту, который их прислал
    #              sock.sendto(data,clients)

