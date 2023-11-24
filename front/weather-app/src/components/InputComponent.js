import React from 'react';

const InputComponent = () => {
    return (
        <>
            <label htmlFor='lattitude'>Lattitude</label>
            <input name='lattitude'></input>
            <label htmlFor="longitude">Longitude</label>
            <input name='longitude'></input>
        </>
    );
};
export default InputComponent;