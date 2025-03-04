import ChatBox from "./ChatBox"
import SendMessage from "./SendMessage"
import Navbar from "./Navbar"

const ChatRoom = () => {
  return (
    <div>
       <Navbar/>
      <ChatBox/>
        <SendMessage/>
    </div>
  )
}

export default ChatRoom