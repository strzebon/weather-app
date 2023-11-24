import React from 'react';
import InputComponent from './InputComponent';
import ButtonComponent from './ButtonComponent';

const FormComponent = () => {
    const search = () => {
        console.log("siema")
    }
    
    return (
        <div>
            <InputComponent/>
            <ButtonComponent clickButton={search}/>
        </div>
    );
};

export default FormComponent;