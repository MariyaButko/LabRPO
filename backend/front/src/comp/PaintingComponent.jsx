import React, { useState } from "react";
import BackendService from "../services/BackendService";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faChevronLeft, faSave } from "@fortawesome/free-solid-svg-icons";

import { Form } from "react-bootstrap";
import { useParams, useNavigate } from 'react-router-dom';

const PaintingComponent = props => {

    const [hidden, setHidden] = useState(false);
    const navigate = useNavigate();
    const [name, setName] = useState("")
    const [year, setYear] = useState()
    const [artistName, setArtist] = useState()
    const [museumName, setMuseum] = useState()
    const [countryName, setCountry] = useState()
    const [id, setId] = useState(useParams().id)

    const updateName = (event) => {
        setName(event.target.value)
    }

    const updateYear = (event) => {
        setYear(event.target.value)
    }

    const updateArtist = (event) => {
        setArtist(event.target.value)
    }
    const updateMuseum = (event) => {
        setMuseum(event.target.value)
    }

    const onSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        let err = null;
        if (name === "") {
            err = "Название должно быть указано"
        }
        let painting = { id: id, name: name,  artist: {"id": "20", "name": artistName}, museum: {"id":"20", "name": museumName}, year: year}
        if (parseInt(id) === -1) {
            BackendService.createPainting(painting)
                .catch(() => { })
        }
        else {
            let painting = {id: id, name: name,  artist: {"id": "20", "name": artistName}, museum: {"id":"20", "name": museumName}, year: year}
            console.log(painting)
            BackendService.updatePainting(painting)
                .catch(() => { })
        }
        navigateToPaintings()
    }

    const navigateToPaintings = () => {
        navigate('/paintings')
    }

    if (hidden)
        return null;
    return (
        <div className="m-4">
            <div className="row my-2 mr-0">
                <h3>Добавить Картину</h3>
                <button
                    className="btn btn-outline-secondary ml-auto"
                    onClick={() => navigateToPaintings()}><FontAwesomeIcon
                    icon={faChevronLeft} />{' '}Назад</button>
            </div>
            <Form onSubmit={onSubmit}>
                <Form.Group>
                    <Form.Label>Название</Form.Label>

                    <Form.Control
                        type="text"
                        placeholder="Введите название картины"
                        onChange={updateName}
                        value={name}
                        name="name"
                        autoComplete="off" />
                    <Form.Control
                        type="int"
                        placeholder="Введите имя художника"
                        onChange={updateArtist}
                        value={artistName}
                        name="name"
                        autoComplete="off" />
                    <Form.Control
                        type="int"
                        placeholder="Введите название музея"
                        onChange={updateMuseum}
                        value={museumName}
                        name="name"
                        autoComplete="off" />
                    <Form.Control
                        type="int"
                        placeholder="Введите год написания"
                        onChange={updateYear}
                        value={year}
                        name="name"
                        autoComplete="off" />
                </Form.Group>
                <button
                    className="btn btn-outline-secondary"
                    type="submit"><FontAwesomeIcon
                    icon={faSave} />{' '}Сохранить</button>
            </Form>
        </div>
    )

}

export default PaintingComponent;