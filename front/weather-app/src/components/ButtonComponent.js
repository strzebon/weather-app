
function ButtonComponent({...props}) {
    
    
    return (
      <>
        <button onClick={()=>{props.clickButton()}}>Szukaj</button>
      </>
    );
  }
  
  export default ButtonComponent;