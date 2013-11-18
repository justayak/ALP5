/*
*	Usage: 
*	Erzeugen eines neuen Prozesses:
*	p1 := MyProcess{}
*
*	initialisieren des Prozesses:
*	boxsize := 10
*	p1.Start(*listofprocesses, boxsize)
*
*	senden von Daten
*	p1.Send("hallo Welt")
*
*	empfangen:
*	result := p1.Recv()
*

Probleme bei dieser Lösung:
	-	Keine Generics -> Die Mailbox wird jeden Datentypen
		akzeptieren
	-	Damit der Process korrekt benutzt werden kann, muss 
		er mit 'Start' initialisiert werden, da es keinen
		ctor gibt
	-	Keine Kapselung der 'Recv'-Methode
*/
package main

import "fmt"

func main() {
	fmt.Println("** starting.. **")

	boxsize := 10
	p1 := MyProcess{}
	p1.Start(nil, boxsize)

	go run(p1)

	p1.Send("hi")
	p1.Send("hi2")
	p1.Send("hi3")
	p1.Send("hi4")
	p1.Send("hi5")
	p1.Send("hi6")

	fmt.Println("** ending.. press enter.. **")
	var i string
	fmt.Scanf("%s", &i)
}

func run(process MyProcess) {
	for {
		fmt.Println(process.Recv())
	}
}

type MyProcess struct {
	name    string
	mailbox chan interface{}
	peers   []MyProcess
}

func (p *MyProcess) Send(message interface{}) {
	p.mailbox <- message

}

func (p *MyProcess) Recv() interface{} {
	return <-p.mailbox
}

func (p *MyProcess) Start(peers []MyProcess, boxsize int) {
	p.mailbox = make(chan interface{}, boxsize)
	p.peers = peers // dummies, they dont do any shit
}
