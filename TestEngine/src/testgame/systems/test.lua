function getRequiredComponents()
	return {"position"}
end

function isPausable()
	return false
end

timer = 0

function update(entity)
  timer = (timer + 0.07) % 360
  entity.position.x = 50 + 50 * math.sin(timer)
end