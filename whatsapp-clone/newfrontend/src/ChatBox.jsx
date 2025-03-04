import Message from "./Message"

const ChatBox = () => {
  
  const messages =[
    {
      id:1,
      text:"hello",
      sender: "User1",
    time: "12:30",
    },
    {
      id:2,
      text:"hi",
      sender: "User2",
    time: "1:30",
    }
  ]

  return (
    <div className="pb-44 pt-20 containerWrap">
     {messages.map(message =>(
      <Message key={message.id} message={message}/>
     ))}
    </div>
  )
}

export default ChatBox