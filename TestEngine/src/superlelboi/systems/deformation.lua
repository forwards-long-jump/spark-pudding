function getRequiredComponents()
	return {"deformation", "position", "size"}
end

function updateEntities(e)
	local rate = 0.3
	local delta = 0.001

	e.deformation.currentVertical = e.deformation.currentVertical * (1 - rate) + rate * e.deformation.vertical

	if math.abs(e.deformation.currentVertical - e.deformation.vertical) < delta then
		e.deformation.currentVertical = e.deformation.vertical
		e.deformation.vertical = 0
	end

	e.deformation.currentHorizontal = e.deformation.currentHorizontal * (1 - rate) + rate * e.deformation.horizontal

	if math.abs(e.deformation.currentHorizontal - e.deformation.horizontal) < delta then
		e.deformation.currentHorizontal = e.deformation.horizontal
		e.deformation.horizontal = 0
	end
end

